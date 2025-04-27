package com.dzhaparov.controller.auth;

import com.dzhaparov.dto.auth.request.RegisterRequest;
import com.dzhaparov.dto.auth.response.RegisterResponse;
import com.dzhaparov.service.auth.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest(null, null, null, null, null));
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model) {
        RegisterResponse response = authService.register(request);

        if (response.success()) {
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Registration failed: " + response.message());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}