package com.dzhaparov.dto.homework.request;

import java.time.LocalDateTime;

public record HomeworkGroupSummaryDto(
        Long lessonId,
        LocalDateTime lessonDate,
        String groupName,
        int totalHomeworks,
        int checkedHomeworks
) {}