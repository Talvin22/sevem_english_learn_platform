package com.dzhaparov.service.student;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
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