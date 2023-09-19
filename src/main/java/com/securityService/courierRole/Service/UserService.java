package com.securityService.courierRole.Service;

import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.dto.UserLoginResponse;
import com.securityService.courierRole.util.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ApiResponse login(UserLoginRequest request);
    ApiResponse<String> registerUser(UserRegistrationRequest request);

    ApiResponse<UserLoginResponse> getUser();

    //ApiResponse<ChangePasswordRequestDto> changePassword(ChangePasswordRequestDto requestDto);
}
