package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.user.User;
import org.springframework.http.HttpStatus;

public record UserDtoDeleteResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        User user) {
    public static String SUCCESS_MASSAGE = "User has been deleted successfully";
    public static String FAILURE_MASSAGE = "User has not been deleted";


    public static UserDtoDeleteResponse of(boolean isUserDeleted, User user) {
        if (isUserDeleted) {
            return new UserDtoDeleteResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MASSAGE,
                    user
            );
        } else {
            return new UserDtoDeleteResponse(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    false,
                    FAILURE_MASSAGE,
                    null
            );
        }
    }
}
