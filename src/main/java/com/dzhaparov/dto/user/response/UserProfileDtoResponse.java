package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.role.Role;

public record UserProfileDtoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        List<GroupShortDto> groups,
        Double salaryPerLesson
) {}