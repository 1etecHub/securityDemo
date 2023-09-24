package com.securityService.courierRole.controller;

import com.securityService.courierRole.Service.UserService;
import com.securityService.courierRole.dto.PasswordRequest;
import com.securityService.courierRole.dto.UserLoginRequest;
import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.events.RegistrationCompleteEvent;
import com.securityService.courierRole.security.VerificationToken;
import com.securityService.courierRole.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid UserRegistrationRequest request, final HttpServletRequest httpRequest) {
        log.info("controller register: register user :: [{}] ::", request.getEmail());
        ApiResponse<User> response = userService.registerUser(request);
        User user = response.getData();
        if (response.isSuccess()) {
            // Create an instance of RegistrationCompleteEvent with the user and any additional data
            RegistrationCompleteEvent registrationEvent = new RegistrationCompleteEvent(user, applicationUrl(httpRequest));
            //publish the event to send a mail to a user after registration
            log.info("was the listener triggered");
            publisher.publishEvent(registrationEvent);
        }
        log.info("Registration successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid UserLoginRequest request) {
        log.info("controller login: login user :: [{}] ::", request.getEmail());
        ApiResponse<String> response = userService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<ApiResponse<?>> verifyRegistration(@RequestParam("token") String token){
        String validToken = userService.validateVerificationToken(token);
        ApiResponse<Object> response = ApiResponse.builder()
                .code(validToken.equalsIgnoreCase("valid") ? "00" : "01")
                .message(validToken.equalsIgnoreCase("valid") ? "user verified successfully" : "verification failed")
                .data(validToken)
                .build();
        HttpStatus httpStatus = validToken.equalsIgnoreCase("valid") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, httpStatus);

    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest httpRequest){
        VerificationToken verificationToken = userService.generateVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(httpRequest), verificationToken);

        return "verification link sent";
    }

    @GetMapping("resetPassword")
    public String resetPassword(@RequestBody PasswordRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userService.findUserByEmail(request.getEmail());
        String url ="";

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(httpRequest), token);

            // Return a success response or perform any necessary actions
            return "resetPasswordSuccess"; // Modify this to return an appropriate success message or view
        } else {
            // Handle the case when the user is not found
            return "resetPasswordFailure"; // Modify this to return an appropriate failure message or view
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("click the link to verify your account: {}", url);
        return url;

    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordRequest request){
        String result = userService.validatePasswordResetToken(token, request);
        if(!result.equalsIgnoreCase("valid")){
            return "invalidToken";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(), request.getNewPassword());
            return "password reset successfully";

        }else{
            return "invalid token";
        }

    }


    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken;
        log.info("click the link to verify your account: {}", url);

    }




    private String applicationUrl(HttpServletRequest httpRequest) {
        return "https://" +httpRequest.getServerName()+":"+ httpRequest.getServerPort()+httpRequest.getServletPath();
    }
}
