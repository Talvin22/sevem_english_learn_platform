package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {

    List<GroupDtoResponse> getGroupsForTeacher(Long teacherId);

    List<UserProfileDtoResponse> getMyStudents(Long teacherId);

    LessonDtoListResponse getMyLessons(Long teacherId);

    HomeworkDtoListResponse getHomeworksToCheck(Long teacherId);

    HomeworkDtoGradeResponse gradeHomework(HomeworkDtoGradeRequest request);

    GroupDtoResponse addStudentToGroup(Long groupId, Long studentId);

    UserProfileDtoResponse getProfile(Long teacherId);
}