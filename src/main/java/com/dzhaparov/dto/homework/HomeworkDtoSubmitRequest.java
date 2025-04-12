package com.dzhaparov.dto.homework;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HomeworkDtoSubmitRequest(
        Long lessonId,
        String content
) {}