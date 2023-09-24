package com.securityService.courierRole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {
    private  String email;
    private String oldPassword;
    private String newPassword;
}
