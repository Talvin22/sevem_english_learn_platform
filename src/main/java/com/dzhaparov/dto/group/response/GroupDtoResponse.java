package com.dzhaparov.dto.group.response;

import com.dzhaparov.entity.group.Group;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

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
    public static final String FAILURE_MESSAGE = "Group could not be found.";

    public static GroupDtoResponse of(boolean isSuccess, Group group) {
        if (isSuccess && group != null) {
            String teacherName = group.getTeacher() != null
                    ? group.getTeacher().getFirst_name() + " " + group.getTeacher().getLast_name()
                    : null;

            List<String> studentNames = group.getStudents() != null
                    ? group.getStudents().stream()
                    .map(user -> user.getFirst_name() + " " + user.getLast_name())
                    .collect(Collectors.toList())
                    : null;

            return new GroupDtoResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    group.getId(),
                    group.getName(),
                    group.getIsActive(),
                    teacherName,
                    null, // Здесь можно добавить расписание группы, если появится
                    studentNames
            );
        } else {
            return new GroupDtoResponse(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
    }
}
