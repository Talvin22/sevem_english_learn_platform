package com.dzhaparov.dto.homework.request;

import java.time.LocalDateTime;

public record HomeworkGroupSummaryDto(
        Long lessonId,
        String groupName,
        LocalDateTime lessonDate,
        int notSubmittedCount,
        int submittedCount,
        int checkedCount
) {}
