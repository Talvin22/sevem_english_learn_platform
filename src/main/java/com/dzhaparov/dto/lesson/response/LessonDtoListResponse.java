package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.LessonStatus;

import java.time.ZonedDateTime;

public record LessonDtoListResponse(
        Long id,
        ZonedDateTime dateUtc,
        String teacherName,
        String studentName,
        String groupName,
        LessonStatus status
) {}