package com.securityService.courierRole.util;


import com.securityService.courierRole.dto.UserRegistrationRequest;
import com.securityService.courierRole.entity.User;
import com.securityService.courierRole.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EntityMapper {
    public User dtoToUser(UserRegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .gender(request.getGender())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .registrationDate(LocalDateTime.now())
                .role(Roles.COURIER_DRIVER)
                .isVerified(true)
                .build();
    }
}
