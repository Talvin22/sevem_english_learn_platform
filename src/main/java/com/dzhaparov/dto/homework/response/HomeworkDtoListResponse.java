package com.dzhaparov.dto.homework.response;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public record HomeworkDtoListResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        List<HomeworkDtoDetailResponse> homeworks
) {
    public static final String SUCCESS_MESSAGE = "Homeworks fetched successfully.";
    public static final String FAILURE_MESSAGE = "No homeworks found.";

    public static HomeworkDtoListResponse of(List<HomeworkDtoDetailResponse> homeworkList) {
        boolean empty = homeworkList == null || homeworkList.isEmpty();

        return new HomeworkDtoListResponse(
                empty ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value(),
                empty ? HttpStatus.NO_CONTENT.getReasonPhrase() : HttpStatus.OK.getReasonPhrase(),
                !empty,
                empty ? FAILURE_MESSAGE : SUCCESS_MESSAGE,
                empty ? Collections.emptyList() : homeworkList
        );
    }
}