package com.dzhaparov.service.home;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;

import java.util.List;
import com.dzhaparov.service.student.StudentService;
import com.dzhaparov.service.teacher.TeacherService;
import org.springframework.stereotype.Service;


@Service
public class HomeServiceImpl implements HomeService {

    private final StudentService studentService;
    private final TeacherService teacherService;

    public HomeServiceImpl(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    public LessonDtoListResponse getLessonsForStudent(Long studentId) {
        return studentService.getMyLessons(studentId);
    }

    @Override
    public LessonDtoListResponse getLessonsForTeacher(Long teacherId) {
        return teacherService.getMyLessons(teacherId);
    }

    @Override
    public GroupDtoResponse getGroupForStudent(Long studentId) {
        return studentService.getMyGroup(studentId);
    }

    @Override
    public List<GroupDtoResponse> getGroupsForTeacher(Long teacherId) {
        return teacherService.getGroupsForTeacher(teacherId);
    }

    @Override
    public HomeworkDtoListResponse getHomeworkSummaryForStudent(Long studentId) {
        return studentService.getMyHomeworks(studentId);
    }

    @Override
    public HomeworkDtoListResponse getHomeworkSummaryForTeacher(Long teacherId) {
        return teacherService.getHomeworksToCheck(teacherId);
    }

    @Override
    public List<UserDtoListResponse> getStudentsForTeacher(Long teacherId) {
        return teacherService.getMyStudents(teacherId);
    }
}