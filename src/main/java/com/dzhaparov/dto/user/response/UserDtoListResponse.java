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
        List<User> userList) {

    public static final String SUCCESS_MESSAGE = "Customer list has been fetched successfully.";
    public static final String FAILURE_MESSAGE = "Customer list has not been found!";

    public static UserDtoListResponse of(boolean isCustomerListEmpty, List<User> customerList) {
        if (isCustomerListEmpty)
            return new UserDtoListResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    false, SUCCESS_MESSAGE, Collections.emptyList());
        else
            return new UserDtoListResponse(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    true, FAILURE_MESSAGE, customerList);
    }
}
