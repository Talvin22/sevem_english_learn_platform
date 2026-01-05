package com.dzhaparov.infrastructure.web.mapper;

import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.infrastructure.web.dto.homework.request.HomeworkGroupSummaryDto;
import com.dzhaparov.infrastructure.web.dto.homework.response.GroupedHomeworkResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkGroupSummaryListResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HomeworkMapper {

    public HomeworkDtoResponse toResponse(Homework homework) {
        return HomeworkDtoResponse.from(homework);
    }

    public HomeworkDtoDetailResponse toDetail(Homework homework) {
        return HomeworkDtoDetailResponse.from(homework);
    }

    public HomeworkDtoGradeResponse toGradeResponse(Homework homework) {
        return HomeworkDtoGradeResponse.of(true, homework.getId(), homework.getStatus(), homework.getGrade());
    }

    public HomeworkDtoSubmitResponse toSubmitResponse(boolean success, Homework homework) {
        return HomeworkDtoSubmitResponse.of(success, homework != null ? homework.getId() : null);
    }

    public HomeworkDtoListResponse toListResponse(List<HomeworkDtoDetailResponse> details) {
        return HomeworkDtoListResponse.of(details);
    }

    public HomeworkGroupSummaryListResponse toSummaryList(List<HomeworkGroupSummaryDto> summary) {
        return HomeworkGroupSummaryListResponse.of(summary);
    }

    public List<HomeworkDtoResponse> toResponses(List<Homework> homeworks) {
        return homeworks.stream().map(HomeworkDtoResponse::from).collect(Collectors.toList());
    }

    public GroupedHomeworkResponse toGroupedResponse(Long lessonId, List<HomeworkDtoResponse> homeworks) {
        return GroupedHomeworkResponse.of(lessonId, homeworks);
    }
}
