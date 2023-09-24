package com.securityService.courierRole.util;


import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EntityMapper {

    private final PasswordEncoder passwordEncoder;


    public User dtoToUser(UserRegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                //encodes the password
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmPassword(passwordEncoder.encode(request.getConfirmPassword()))
                .gender(request.getGender())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .registrationDate(LocalDateTime.now())
                .role(Roles.COURIER_DRIVER)
                .build();
    }
}
