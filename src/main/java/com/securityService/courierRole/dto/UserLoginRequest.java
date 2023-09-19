package com.securityService.courierRole.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {

    @NotEmpty(message = "password cannot be empty")
    private String password;
    @NotEmpty(message = "please email cannot be empty")
    @Email
    private String email;
}
