package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.user.User;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public record UserDtoListResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        List<User> userList
) {
    public static final String SUCCESS_MESSAGE = "Customer list has been fetched successfully.";
    public static final String FAILURE_MESSAGE = "Customer list has not been found!";

    public static UserDtoListResponse of(List<User> userList) {
        if (userList != null && !userList.isEmpty()) {
            return new UserDtoListResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    userList
            );
        } else {
            return new UserDtoListResponse(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    Collections.emptyList()
            );
        }
    }
}