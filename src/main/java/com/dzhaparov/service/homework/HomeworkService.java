package com.dzhaparov.service.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.entity.user.User;

import java.util.List;

public interface HomeworkService {
    List<HomeworkDtoResponse> createHomework(CreateHomeworkRequest request, Long teacherId);

    List<HomeworkDtoResponse> getHomeworksForStudent(Long studentId);

    List<HomeworkDtoResponse> getHomeworksToCheckForTeacher(Long teacherId);

    HomeworkDtoResponse getHomeworkById(Long id, User user);
}