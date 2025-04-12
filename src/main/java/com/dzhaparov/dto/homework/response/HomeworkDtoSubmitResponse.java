package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.Homework;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record HomeworkDtoSubmitResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long id,
        Long lessonId,
        String content,
        String status,
        LocalDateTime submittedAt
) {
    public static final String SUCCESS_MESSAGE = "Homework has been submitted successfully.";
    public static final String FAILURE_MESSAGE = "Homework submission failed.";

    public static HomeworkDtoSubmitResponse of(boolean isSuccess, Homework homework) {
        if (isSuccess) {
            return new HomeworkDtoSubmitResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    homework.getId(),
                    homework.getLesson().getId(),
                    homework.getContent(),
                    homework.getStatus().name(),
                    homework.getSubmittedAt()
            );
        } else {
            return new HomeworkDtoSubmitResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
    }
}