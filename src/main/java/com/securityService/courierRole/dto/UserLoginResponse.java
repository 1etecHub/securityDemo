package com.securityService.courierRole.dto;

import com.securityService.courierRole.enums.Roles;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginResponse {
    private String firstName;
    private String lastName;
    private String token;
    private String email;
    private Roles role;
}
