package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.Homework;
import org.springframework.http.HttpStatus;

public record HomeworkDtoGradeResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        Long homeworkId,
        int grade,
        String status
) {
    public static final String SUCCESS_MESSAGE = "Homework graded successfully.";
    public static final String FAILURE_MESSAGE = "Grading failed.";

    public static HomeworkDtoGradeResponse of(boolean success, Homework homework) {
        if (success) {
            return new HomeworkDtoGradeResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    SUCCESS_MESSAGE,
                    homework.getId(),
                    homework.getGrade(),
                    homework.getStatus().name()
            );
        } else {
            return new HomeworkDtoGradeResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    false,
                    FAILURE_MESSAGE,
                    null, 0, null
            );
        }
    }
}