package com.dzhaparov.dto.homework.request;

public record HomeworkDtoGradeRequest(
        Long homeworkId,
        int grade
) {}