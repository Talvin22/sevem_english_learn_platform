package com.dzhaparov.dto.homework.request;

public record CreateHomeworkRequest(
        Long lessonId,
        Long groupId,
        Long studentId,
        String content
) {}
