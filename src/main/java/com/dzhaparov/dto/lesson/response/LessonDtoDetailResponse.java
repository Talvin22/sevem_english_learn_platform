package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.CancelingReasons;

import java.time.ZonedDateTime;

public record LessonDtoDetailResponse(
        Long id,
        String teacherFullName,
        String studentFullName,
        String groupName,
        ZonedDateTime dateUtc,
        LessonStatus status,
        CancelingReasons cancelingReason,
        String cancelledBy
) {}