package com.dzhaparov.dto.homework.response;

import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;

import java.time.LocalDateTime;

public record HomeworkDtoResponse(
        Long id,
        String content,
        HomeworkStatus status,
        Integer grade,
        LocalDateTime submittedAt,
        Long studentId,
        String studentFullName,
        Long groupId,
        String groupName,
        Long lessonId
) {
    public static HomeworkDtoResponse from(Homework hw) {
        return new HomeworkDtoResponse(
                hw.getId(),
                hw.getContent(),
                hw.getStatus(),
                hw.getGrade(),
                hw.getSubmittedAt(),
                hw.getStudent() != null ? hw.getStudent().getId() : null,
                hw.getStudent() != null ? hw.getStudent().getFirst_name() + " " + hw.getStudent().getLast_name() : null,
                hw.getGroup() != null ? hw.getGroup().getId() : null,
                hw.getGroup() != null ? hw.getGroup().getName() : null,
                hw.getLesson() != null ? hw.getLesson().getId() : null
        );
    }
}
