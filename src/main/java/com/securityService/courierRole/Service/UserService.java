package com.securityService.courierRole.Service;

import com.securityService.courierRole.dto.PasswordRequest;
import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.dto.UserLoginResponse;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.security.VerificationToken;
import com.securityService.courierRole.util.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    ApiResponse login(UserLoginRequest request);
    ApiResponse<User> registerUser(UserRegistrationRequest request);

    ApiResponse<UserLoginResponse> getUser();

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateVerificationToken(String oldToken);

    Optional<User> findUserByEmail(String email);



    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token, PasswordRequest request);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);



    //ApiResponse<ChangePasswordRequestDto> changePassword(ChangePasswordRequestDto requestDto);
}
