package com.dzhaparov.application.service.home;


import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoResponse;
import com.dzhaparov.infrastructure.web.dto.home.response.HomePageDataResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public interface HomeService {

    HomePageDataResponse getHomePageData(String email);

    LessonDtoListResponse getLessonsForStudent(Long studentId);

    LessonDtoListResponse getLessonsForTeacher(Long teacherId);

    GroupDtoResponse getGroupForStudent(Long studentId);

    List<GroupShortDto> getGroupsForTeacher(Long teacherId);

    HomeworkDtoListResponse getHomeworkSummaryForStudent(Long studentId);

    HomeworkDtoListResponse getHomeworkSummaryForTeacher(Long teacherId);

    List<UserProfileDtoResponse> getStudentsForTeacher(Long teacherId);
}