package com.dzhaparov.dto.lesson.response;


import java.util.List;

public record LessonDtoListResponse(
        List<LessonDtoDetailResponse> lessons
) {
}