package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonStatus;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

// Confirmation of successful lesson creation
public record LessonDtoCreateResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long id,
        Long teacherId,
        Long studentId,
        Long groupId,
        ZonedDateTime dateUtc,
        LessonStatus status
) {
    public static final String SUCCESS_MESSAGE = "Lesson created successfully.";
    public static final String FAILURE_MESSAGE = "Lesson creation failed.";

    public static LessonDtoCreateResponse of(boolean success, Lesson lesson) {
        if (success) {
            return new LessonDtoCreateResponse(
                    HttpStatus.CREATED.value(),
                    HttpStatus.CREATED.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    lesson.getId(),
                    lesson.getTeacher().getId(),
                    lesson.getStudent() != null ? lesson.getStudent().getId() : null,
                    lesson.getGroup() != null ? lesson.getGroup().getId() : null,
                    lesson.getDateUtc(),
                    lesson.getStatus()
            );
        } else {
            return new LessonDtoCreateResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    null, null, null, null, null, null
            );
        }
    }
}