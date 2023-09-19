package com.securityService.courierRole.controller;

import com.securityService.courierRole.Service.UserService;
import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        log.info("controller register: register user :: [{}] ::", request.getEmail());
        ApiResponse<String> response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody @Valid UserLoginRequest request) {
        log.info("controller login: login user :: [{}] ::", request.getEmail());
        ApiResponse<String> response = userService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
