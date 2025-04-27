package com.dzhaparov.dto.auth.response;


import org.springframework.http.HttpStatus;

public record RegisterResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long userId
) {
    public static final String SUCCESS_MESSAGE = "Registration successful.";
    public static final String FAILURE_MESSAGE = "Registration failed.";

    public static RegisterResponse success(Long userId) {
        return new RegisterResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                true,
                SUCCESS_MESSAGE,
                userId
        );
    }

    public static RegisterResponse failure() {
        return new RegisterResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                false,
                FAILURE_MESSAGE,
                null
        );
    }
}