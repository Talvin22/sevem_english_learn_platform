package com.dzhaparov.dto.home.response;


import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;

import java.util.List;

public record HomePageDataResponse(
        LessonDtoListResponse lessons,
        HomeworkDtoListResponse homeworks,
        GroupDtoResponse studentGroup,
        List<UserDtoListResponse> teacherStudents,
        List<GroupDtoResponse> teacherGroups
) {}