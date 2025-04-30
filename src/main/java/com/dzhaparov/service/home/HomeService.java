package com.dzhaparov.service.home;


import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.home.response.HomePageDataResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public interface HomeService {

    HomePageDataResponse getHomePageData(String email);

    LessonDtoListResponse getLessonsForStudent(Long studentId);

    LessonDtoListResponse getLessonsForTeacher(Long teacherId);

    GroupDtoResponse getGroupForStudent(Long studentId);

    List<GroupDtoResponse> getGroupsForTeacher(Long teacherId);

    HomeworkDtoListResponse getHomeworkSummaryForStudent(Long studentId);

    HomeworkDtoListResponse getHomeworkSummaryForTeacher(Long teacherId);

    List<UserProfileDtoResponse> getStudentsForTeacher(Long teacherId);
}