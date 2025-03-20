package com.dzhaparov.dto.user.response;


import com.dzhaparov.entity.user.User;
import org.springframework.http.HttpStatus;

public record UserDtoCreateResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        User user
) {

    public static String SUCCESS_MASSAGE = "User has been created successfully";
    public static String FAILURE_MASSAGE = "User has not been created";


    public static UserDtoCreateResponse of(boolean isUserCreated, User user) {
        if (isUserCreated) {
            return new UserDtoCreateResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MASSAGE,
                    user
            );
        } else {
            return new UserDtoCreateResponse(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    false,
                    FAILURE_MASSAGE,
                    null
            );
        }
    }
}

