package com.dzhaparov.dto.lesson.response;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public record LessonDtoListResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        List<LessonDtoDetailResponse> lessons
) {
    public static final String SUCCESS_MESSAGE = "Lessons fetched successfully.";
    public static final String FAILURE_MESSAGE = "No lessons found.";

    public static LessonDtoListResponse of(List<LessonDtoDetailResponse> lessonList) {
        boolean empty = lessonList == null || lessonList.isEmpty();

        return new LessonDtoListResponse(
                empty ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value(),
                empty ? HttpStatus.NO_CONTENT.getReasonPhrase() : HttpStatus.OK.getReasonPhrase(),
                !empty,
                empty ? FAILURE_MESSAGE : SUCCESS_MESSAGE,
                empty ? Collections.emptyList() : lessonList
        );
    }
}