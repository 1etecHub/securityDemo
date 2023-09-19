package com.securityService.courierRole.ServiceImplementation;

import com.securityService.courierRole.Service.UserService;
import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.dto.UserLoginResponse;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.exceptions.UserNotFoundException;
import com.securityService.courierRole.repository.UserRepo;
import com.securityService.courierRole.util.ApiResponse;
import com.securityService.courierRole.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EntityMapper entityMapper;

    @Override
    public ApiResponse login(UserLoginRequest request) {

        log.info("Request to login at the service layer");
        User appUser = userRepo.findByEmail(request.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User not found"));
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
    public ApiResponse<String> registerUser(UserRegistrationRequest request) {
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
}
