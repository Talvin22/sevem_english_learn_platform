package com.dzhaparov.dto.group.response;

import com.dzhaparov.entity.group.Group;
import org.springframework.http.HttpStatus;

public record GroupDtoResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long id,
        String name
) {
    public static final String SUCCESS_MESSAGE = "Group fetched successfully.";
    public static final String FAILURE_MESSAGE = "Group could not be found.";

    public static GroupDtoResponse of(boolean isSuccess, Group group) {
        if (isSuccess && group != null) {
            return new GroupDtoResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    group.getId(),
                    group.getName()
            );
        } else {
            return new GroupDtoResponse(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    null,
                    null
            );
        }
    }
}