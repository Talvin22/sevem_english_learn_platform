package com.dzhaparov.application.service.student;

import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoResponse;
import com.dzhaparov.infrastructure.web.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.user.response.UserProfileDtoResponse;
import org.springframework.stereotype.Service;

import java.time.ZoneId;


@Service
public interface StudentService {

    LessonDtoListResponse getMyLessons(Long studentId);

    HomeworkDtoListResponse getMyHomeworks(Long studentId);

    HomeworkDtoSubmitResponse submitHomework(Long studentId, HomeworkDtoSubmitRequest request);

    GroupDtoResponse getMyGroup(Long studentId);

    UserProfileDtoResponse getProfile(Long userId);
}