package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.role.Role;
import java.util.List;

public record UserProfileDtoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        List<String> groupNames,
        Double salaryPerLesson
) {}