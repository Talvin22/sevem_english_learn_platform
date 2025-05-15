package com.dzhaparov.dto.homework.response;

import java.time.LocalDateTime;
import java.util.List;

public record GroupedHomeworkResponse(
        Long lessonId,
        String groupName,
        LocalDateTime lessonDate,
        List<HomeworkDtoDetailResponse> homeworks
) {
}
