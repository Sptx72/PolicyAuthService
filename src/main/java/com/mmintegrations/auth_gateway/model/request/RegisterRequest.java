package com.mmintegrations.auth_gateway.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private String role;
}
