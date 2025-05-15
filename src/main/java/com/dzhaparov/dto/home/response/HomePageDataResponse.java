package com.dzhaparov.dto.home.response;


import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkGroupSummaryListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public record HomePageDataResponse(
        LessonDtoListResponse lessons,
        HomeworkDtoListResponse homeworks,
        GroupDtoResponse studentGroup,
        List<UserProfileDtoResponse> teacherStudents,
        List<GroupShortDto> teacherGroups,
        HomeworkGroupSummaryListResponse groupedHomeworks
) {}