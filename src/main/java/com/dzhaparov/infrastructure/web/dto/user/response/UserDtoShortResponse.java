package com.dzhaparov.infrastructure.web.dto.user.response;

public record UserDtoShortResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {}