package com.dzhaparov.dto.homework.response;

import java.util.List;

public record HomeworkDtoListResponse(
        List<HomeworkDtoDetailResponse> homeworks
) {}