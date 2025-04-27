package com.dzhaparov.controller.auth;

import com.dzhaparov.dto.auth.request.RegisterRequest;
import com.dzhaparov.dto.auth.response.RegisterResponse;
import com.dzhaparov.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}