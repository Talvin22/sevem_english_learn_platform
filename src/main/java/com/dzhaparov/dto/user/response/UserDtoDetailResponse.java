package com.dzhaparov.dto.user.response;


public record UserDtoDetailResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {}