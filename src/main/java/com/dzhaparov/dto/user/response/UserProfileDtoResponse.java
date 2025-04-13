package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.role.Role;
import com.dzhaparov.entity.group.Group;

public record UserProfileDtoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Group group,
        Double salaryPerLesson
) {}
