package com.dzhaparov.service.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;

import java.util.List;

public interface HomeworkService {
    HomeworkDtoResponse createHomework(CreateHomeworkRequest request);

    List<HomeworkDtoResponse> getHomeworksForStudent(Long studentId);

    List<HomeworkDtoResponse> getHomeworksToCheckForTeacher(Long teacherId);
}