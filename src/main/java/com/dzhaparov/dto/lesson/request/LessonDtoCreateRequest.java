package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.LessonStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

// Teacher/student create a lesson
@JsonIgnoreProperties(ignoreUnknown = true)
public record LessonDtoCreateRequest(
        Long teacherId,
        Long studentId,
        Long groupId,
        ZonedDateTime dateUtc,
        LessonStatus status
) {}
