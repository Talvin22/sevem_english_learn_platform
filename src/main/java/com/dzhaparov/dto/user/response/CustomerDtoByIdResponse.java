package com.dzhaparov.dto.user.response;


import com.dzhaparov.entity.user.User;
import org.springframework.http.HttpStatus;

public record CustomerDtoByIdResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        User user) {

    public static final String SUCCESS_MESSAGE = "Customer with id %s has been fetched successfully.";
    public static final String FAILURE_MESSAGE = "Customer with id %s has not been found!";

    public static CustomerDtoByIdResponse of(Long id, boolean isUserFound, User user) {
        if (isUserFound)
            return new CustomerDtoByIdResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true, SUCCESS_MESSAGE.formatted(id), user);
        else
            return new CustomerDtoByIdResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    false, FAILURE_MESSAGE.formatted(id), null);
    }
}