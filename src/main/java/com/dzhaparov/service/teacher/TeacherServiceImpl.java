package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.role.Role;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Override
    public List<UserDtoListResponse> getMyStudents(Long teacherId) {

        return Collections.emptyList();
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long teacherId) {

        return new LessonDtoListResponse(Collections.emptyList());
    }

    @Override
    public HomeworkDtoListResponse getHomeworksToCheck(Long teacherId) {

        return new HomeworkDtoListResponse(
                null
        );
    }

    @Override
    public HomeworkDtoGradeResponse gradeHomework(HomeworkDtoGradeRequest request) {

        return new HomeworkDtoGradeResponse(
                0,
                null,  // grade
                false,
                null,
                0L,
                0,
                null
        );
    }

    @Override
    public GroupDtoResponse addStudentToGroup(Long groupId, Long studentId) {

        return new GroupDtoResponse(
                0,
                "New Group",
                true,
                "Group description",
                0L,
                "Teacher Name"
        );
    }

    @Override
    public UserProfileDtoResponse getProfile(Long teacherId) {

        return new UserProfileDtoResponse(
                teacherId,
                "TeacherFirstName",
                "TeacherLastName",
                "teacher@example.com",
                Role.TEACHER,
                null,
                100.0
        );
    }

    @Override
    public List<GroupDtoResponse> getGroupsForTeacher(Long teacherId) {

        return Collections.emptyList();
    }
}