package com.mmintegrations.auth_gateway.controller;

import com.mmintegrations.auth_gateway.model.request.AuthenticationRequest;
import com.mmintegrations.auth_gateway.model.request.RegisterRequest;
import com.mmintegrations.auth_gateway.service.AuthenticationService;
import com.mmintegrations.auth_gateway.model.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.authenticate(request));
    }



}
