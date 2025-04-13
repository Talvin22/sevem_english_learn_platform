package com.dzhaparov.service.student;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public interface StudentService {

    List<LessonDtoListResponse> getMyLessons(Long studentId);

    List<HomeworkDtoListResponse> getMyHomeworks(Long studentId);

    HomeworkDtoSubmitResponse submitHomework(Long studentId, HomeworkDtoSubmitRequest request);

    GroupDtoResponse getMyGroup(Long studentId);

    UserProfileDtoResponse getProfile(Long userId);
}