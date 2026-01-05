package com.dzhaparov.infrastructure.web.dto.homework.request;

public record CreateHomeworkRequest(
        Long lessonId,
        Long groupId,
        Long studentId,
        String content
) {}
