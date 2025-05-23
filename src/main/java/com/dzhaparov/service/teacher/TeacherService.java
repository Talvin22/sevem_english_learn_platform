package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.request.CreateGroupRequest;
import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.GroupedHomeworkResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkGroupSummaryListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public interface TeacherService {

    List<GroupShortDto> getGroupsForTeacher(Long teacherId);

    List<UserProfileDtoResponse> getMyStudents(Long teacherId);

    LessonDtoListResponse getMyLessons(Long teacherId);

    HomeworkDtoListResponse getHomeworksToCheck(Long teacherId);

    HomeworkDtoGradeResponse gradeHomework(HomeworkDtoGradeRequest request);

    GroupDtoResponse addStudentToGroup(Long groupId, Long studentId);

    UserProfileDtoResponse getProfile(Long teacherId);

    void removeStudentFromGroup(Long groupId, Long studentId);
    List<UserProfileDtoResponse> getStudentsWithoutGroups();
    GroupDtoResponse createGroup(CreateGroupRequest request);

    void deleteGroup(Long groupId);
    void assignStudentToTeacher(Long teacherId, Long studentId);
    void unassignStudentFromTeacher(Long studentId);
    HomeworkGroupSummaryListResponse getGroupedHomeworksToCheck(Long teacherId);
    HomeworkDtoGradeResponse updateHomeworkAsTeacher(HomeworkDtoGradeRequest request, Long teacherId);
}