package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.LessonStatus;
import java.time.ZonedDateTime;
import java.util.List;

public record LessonShortCardResponse(
        Long id,
        ZonedDateTime dateUtc,
        String groupName,
        List<String> studentNames,
        LessonStatus status
) {
}