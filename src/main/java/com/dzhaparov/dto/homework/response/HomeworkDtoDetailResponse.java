package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;

import java.time.LocalDateTime;

public record HomeworkDtoDetailResponse(
        Long id,
        Long lessonId,
        LocalDateTime lessonDate,
        HomeworkStatus status,
        Integer grade,
        String content,
        String groupName
) {
    public static HomeworkDtoDetailResponse from(Homework hw) {
        return new HomeworkDtoDetailResponse(
                hw.getId(),
                hw.getLesson().getId(),
                hw.getLesson().getDateUtc().toLocalDateTime(),
                hw.getStatus(),
                hw.getGrade(),
                hw.getContent(),
                hw.getGroup() != null ? hw.getGroup().getName() : null
        );
    }
}