package com.dzhaparov.service.auth;


import com.dzhaparov.dto.auth.request.RegisterRequest;
import com.dzhaparov.dto.auth.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}