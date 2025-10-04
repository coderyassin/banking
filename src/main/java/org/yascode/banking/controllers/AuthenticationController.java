package org.yascode.banking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.banking.dto.AuthenticationRequest;
import org.yascode.banking.dto.AuthenticationResponse;
import org.yascode.banking.dto.UserDto;
import org.yascode.banking.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDto user) {

        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

}
