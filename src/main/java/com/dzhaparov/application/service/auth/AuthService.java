package com.dzhaparov.application.service.auth;


import com.dzhaparov.infrastructure.web.dto.auth.request.RegisterRequest;
import com.dzhaparov.infrastructure.web.dto.auth.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}