package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;

import java.util.List;

public interface TeacherService {

    List<UserDtoListResponse> getMyStudents(Long teacherId);

    List<LessonDtoListResponse> getMyLessons(Long teacherId);

    List<HomeworkDtoListResponse> getHomeworksToCheck(Long teacherId);

    HomeworkDtoGradeResponse gradeHomework(HomeworkDtoGradeRequest request);

    GroupDtoResponse addStudentToGroup(Long groupId, Long studentId);

    UserProfileDtoResponse getProfile(Long teacherId);
}