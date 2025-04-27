package com.dzhaparov.dto.auth.request;

import com.dzhaparov.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
) {}
