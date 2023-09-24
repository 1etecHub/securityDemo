package com.securityService.courierRole.ServiceImplementation;

import com.securityService.courierRole.Service.UserService;
import com.securityService.courierRole.dto.PasswordRequest;
import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.dto.UserLoginResponse;
import com.securityService.courierRole.entity.PasswordRestToken;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.exceptions.UserNotFoundException;
import com.securityService.courierRole.repository.PasswordResetTokenRepo;
import com.securityService.courierRole.repository.UserRepo;
import com.securityService.courierRole.repository.VerificationTokenRepo;
import com.securityService.courierRole.security.VerificationToken;
import com.securityService.courierRole.util.ApiResponse;
import com.securityService.courierRole.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepo verificationTokenRepo;
    private final EntityMapper entityMapper;
    private final PasswordResetTokenRepo passwordResetTokenRepo;


    @Override
    public ApiResponse login(UserLoginRequest request) {

        log.info("Request to login at the service layer");
        User appUser = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));



        appUser.setLastLoginDate(LocalDateTime.now());

        User user = userRepo.save(appUser);
        UserLoginResponse userResponse = UserLoginResponse.builder()
                .email(request.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();

        return ApiResponse.builder()
                .message("successfully logged in")
                .data(userResponse)
                .code("00")
                .httpStatus(HttpStatus.OK)
                .build();


    }

    @Override
    public ApiResponse<User> registerUser(UserRegistrationRequest request) {
        log.info("check if the user already exists");
        boolean checkForUser = userRepo.existsByEmail(request.getEmail());
        if (checkForUser){
            return new ApiResponse<>("01", "email already taken", HttpStatus.BAD_REQUEST);
        }
        log.info("createTheCourierDriver");
        User newUser = entityMapper.dtoToUser(request);
        userRepo.save(newUser);
        log.info("User successfully saved");
        return new ApiResponse<>("00", "user successfully created", HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<UserLoginResponse> getUser() {
        return null;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        }
        user.setVerified(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepo.save(verificationToken);
        return verificationToken;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordRestToken passwordRestToken = new PasswordRestToken(user, token);
        passwordResetTokenRepo.save(passwordRestToken);

    }

    @Override
    public String validatePasswordResetToken(String token, PasswordRequest request) {

        PasswordRestToken passwordRestToken = passwordResetTokenRepo.findByToken(token);
        if (passwordRestToken == null) {
            return "invalid";
        }
        User user = passwordRestToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(passwordRestToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            passwordResetTokenRepo.delete(passwordRestToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {

        return Optional.ofNullable(passwordResetTokenRepo.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }


}
