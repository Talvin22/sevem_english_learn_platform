package com.dzhaparov.dto.user.response;

import com.dzhaparov.entity.user.User;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record UserDtoListResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        List<UserDtoShortResponse> users
) {
    public static final String SUCCESS_MESSAGE = "Students fetched successfully.";
    public static final String FAILURE_MESSAGE = "No students found.";

    public static UserDtoListResponse of(List<User> userList) {
        boolean empty = userList == null || userList.isEmpty();

        List<UserDtoShortResponse> mappedUsers = empty ? Collections.emptyList() :
                userList.stream()
                        .map(user -> new UserDtoShortResponse(
                                user.getId(),
                                user.getFirst_name(),
                                user.getLast_name(),
                                user.getEmail()
                        ))
                        .collect(Collectors.toList());

        return new UserDtoListResponse(
                empty ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value(),
                empty ? HttpStatus.NO_CONTENT.getReasonPhrase() : HttpStatus.OK.getReasonPhrase(),
                !empty,
                empty ? FAILURE_MESSAGE : SUCCESS_MESSAGE,
                mappedUsers
        );
    }
}