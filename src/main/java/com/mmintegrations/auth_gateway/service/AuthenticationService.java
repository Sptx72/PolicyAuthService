package com.mmintegrations.auth_gateway.service;

import com.mmintegrations.auth_gateway.config.JwtService;
import com.mmintegrations.auth_gateway.model.exception.RolePermissionDeniedException;
import com.mmintegrations.auth_gateway.model.request.AuthenticationRequest;
import com.mmintegrations.auth_gateway.model.request.RegisterRequest;
import com.mmintegrations.auth_gateway.repository.RoleRepository;
import com.mmintegrations.auth_gateway.model.Role;
import com.mmintegrations.auth_gateway.model.User;
import com.mmintegrations.auth_gateway.model.response.AuthenticationResponse;
import com.mmintegrations.auth_gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {

        Role role = roleRepository.findByName(request.getRole()).orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        Map<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("role", role);

        String jwtToken = jwtService.generateToken(extraClaim, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        Role role = roleRepository.findByName(request.getRole()).orElseThrow();

        if (!userHasRole(user.getId(), role.getId()))
            throw new RolePermissionDeniedException("The user [" + user.getEmail() + "] has no permissions for the role [" + request.getRole() + "]");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Map<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("role", role);

        String jwtToken = jwtService.generateToken(extraClaim, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean userHasRole(UUID userId, int roleId) {
        return userRepository.hasRole(userId, roleId) == 1;
    }
}
