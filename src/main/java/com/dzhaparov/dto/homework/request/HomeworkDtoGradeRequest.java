package com.dzhaparov.dto.homework.request;

import com.dzhaparov.entity.homework.HomeworkStatus;

public record HomeworkDtoGradeRequest(
        HomeworkStatus status,
        Integer grade
) {}