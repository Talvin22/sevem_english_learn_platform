package com.dzhaparov.infrastructure.web.dto.homework.request;

public record HomeworkDtoGradeRequest(
        Long homeworkId,
        Integer grade,
        String content
) {}