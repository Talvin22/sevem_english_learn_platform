package com.dzhaparov.infrastructure.web.dto.homework.request;

import java.time.LocalDateTime;

public record HomeworkGroupSummaryDto(
        Long lessonId,
        String studentName,
        LocalDateTime lessonDate,
        String groupName,
        int totalHomeworks,
        int checkedHomeworks
) {}