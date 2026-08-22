package com.gabriaum.picpay.user.controller;

import com.gabriaum.picpay.user.dto.LoginDTO;
import com.gabriaum.picpay.user.dto.RegisterDTO;
import com.gabriaum.picpay.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> onLogin(
            @RequestBody LoginDTO loginDTO
    ) {
        return ResponseEntity.ok(service.authenticate(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> onRegister(
            @RequestBody RegisterDTO registerDTO
    ) {
        return ResponseEntity
                .status(201)
                .body(service.register(registerDTO));
    }
}