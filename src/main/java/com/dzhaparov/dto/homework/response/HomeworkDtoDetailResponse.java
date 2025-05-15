package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record HomeworkDtoDetailResponse(
        Long id,
        Long lessonId,
        LocalDateTime lessonDate,
        String groupName,
        HomeworkStatus status,
        Integer grade,
        String content
) {
    public static HomeworkDtoDetailResponse from(Homework hw) {
        return new HomeworkDtoDetailResponse(
                hw.getId(),
                hw.getLesson().getId(),
                hw.getLesson().getDateUtc().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                hw.getLesson().getGroup() != null ? hw.getLesson().getGroup().getName() : null,
                hw.getStatus(),
                hw.getGrade(),
                hw.getContent()
        );
    }
}