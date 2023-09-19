package com.securityService.courierRole.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationResponse {
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String email;
}
