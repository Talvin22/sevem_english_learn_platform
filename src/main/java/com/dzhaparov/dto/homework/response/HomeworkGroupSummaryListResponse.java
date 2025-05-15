package com.dzhaparov.dto.homework.response;

import com.dzhaparov.dto.homework.request.HomeworkGroupSummaryDto;

import java.util.List;

public record HomeworkGroupSummaryListResponse(
        List<HomeworkGroupSummaryDto> homeworks
) {
    public static HomeworkGroupSummaryListResponse of(List<HomeworkGroupSummaryDto> list) {
        return new HomeworkGroupSummaryListResponse(list);
    }
}