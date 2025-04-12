package com.dzhaparov.dto.homework.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HomeworkDtoSubmitRequest(
        Long lessonId,
        String content
) {}