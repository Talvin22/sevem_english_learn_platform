package com.dzhaparov.service.auth;

import com.dzhaparov.dto.auth.request.RegisterRequest;
import com.dzhaparov.dto.auth.response.RegisterResponse;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return RegisterResponse.failure();
        }

        User user = new User();
        user.setFirst_name(request.firstName());
        user.setLast_name(request.lastName());
        user.setEmail(request.email());
        user.setHashed_password(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        userRepository.save(user);

        return RegisterResponse.success(user.getId());
    }
}