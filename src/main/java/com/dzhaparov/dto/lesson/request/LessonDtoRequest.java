package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LessonDtoRequest(
        Long id,
        User teacher,
        User student,
        ZonedDateTime dateUtc,
        LessonStatus status,
        CancelingReasons cancelingReasons
) {
}
