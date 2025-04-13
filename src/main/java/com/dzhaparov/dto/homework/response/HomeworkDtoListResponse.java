package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.HomeworkStatus;

import java.time.LocalDateTime;

public record HomeworkDtoListResponse(
        Long id,
        Long lessonId,
        LocalDateTime lessonDate,
        HomeworkStatus status,
        Integer grade
) {}