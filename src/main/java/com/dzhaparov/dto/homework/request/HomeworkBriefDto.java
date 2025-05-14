package com.dzhaparov.dto.homework.request;

import com.dzhaparov.entity.homework.HomeworkStatus;


import com.dzhaparov.entity.homework.Homework;

public record HomeworkBriefDto(
        Long id,
        HomeworkStatus status,
        Integer grade
) {
    public static HomeworkBriefDto from(Homework homework) {
        return new HomeworkBriefDto(
                homework.getId(),
                homework.getStatus(),
                homework.getGrade()
        );
    }
}