package com.dzhaparov.dto.group.response;

import com.dzhaparov.entity.group.Group;
import org.springframework.http.HttpStatus;

import java.util.List;

public record GroupDtoResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long id,
        String name,
        Boolean isActive,
        String teacherFullName,
        String groupSchedule,
        List<String> students
) {
    public static final String SUCCESS_MESSAGE = "Group fetched successfully.";
    public static final String FAILURE_MESSAGE = "Group not found.";

    // в GroupDtoResponse.java
    public static GroupDtoResponse ofSuccess(Group group) {
        return new GroupDtoResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                true,
                "Group fetched successfully.",
                group.getId(),
                group.getName(),
                group.getActive(),
                group.getTeacher().getFirst_name() + " " + group.getTeacher().getLast_name(),
                null,
                null
        );
    }

    public static GroupDtoResponse of(boolean isSuccess, Group group) {
        if (isSuccess && group != null) return ofSuccess(group);
        else return new GroupDtoResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                false,
                "Group not found.",
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}