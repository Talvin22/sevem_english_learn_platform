package com.dzhaparov.infrastructure.web.dto.user.response;

import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.entity.role.Role;

import java.util.List;

public record UserProfileDtoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        List<GroupShortDto> groups,
        Double salaryPerLesson
) {}