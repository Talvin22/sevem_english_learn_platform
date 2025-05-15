package com.dzhaparov.service.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.*;
import com.dzhaparov.entity.user.User;

import java.util.List;

public interface HomeworkService {
    List<HomeworkDtoResponse> createHomework(CreateHomeworkRequest request, Long teacherId);

    List<HomeworkDtoResponse> getHomeworksForStudent(Long studentId);

    List<HomeworkDtoResponse> getHomeworksToCheckForTeacher(Long teacherId);

    HomeworkDtoDetailResponse getHomeworkById(Long id, User user);

    HomeworkGroupSummaryListResponse getGroupedHomeworksToCheck(Long teacherId);

    HomeworkDtoListResponse getHomeworksByLessonId(Long lessonId);
    HomeworkDtoResponse updateHomework(HomeworkDtoGradeRequest request);
    void submitHomework(Long homeworkId, Long studentId);

}