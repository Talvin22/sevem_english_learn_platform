package com.dzhaparov.dto.user.response;

public record UserDtoShortResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {}