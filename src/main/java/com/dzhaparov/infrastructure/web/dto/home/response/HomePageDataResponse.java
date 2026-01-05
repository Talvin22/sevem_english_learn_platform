package com.dzhaparov.infrastructure.web.dto.home.response;


import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkGroupSummaryListResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public record HomePageDataResponse(
        LessonDtoListResponse lessons,
        HomeworkDtoListResponse homeworks,
        GroupDtoResponse studentGroup,
        List<UserProfileDtoResponse> teacherStudents,
        List<GroupShortDto> teacherGroups,
        HomeworkGroupSummaryListResponse groupedHomeworks
) {}