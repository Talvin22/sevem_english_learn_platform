package com.dzhaparov.service.home;

import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.home.response.HomePageDataResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.role.Role;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.service.student.StudentService;
import com.dzhaparov.service.teacher.TeacherService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HomeServiceImpl implements HomeService {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserRepository userRepository;

    public HomeServiceImpl(StudentService studentService, TeacherService teacherService, UserRepository userRepository) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userRepository = userRepository;
    }

    @Override
    public HomePageDataResponse getHomePageData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() == Role.STUDENT) {
            return new HomePageDataResponse(
                    studentService.getMyLessons(user.getId()),
                    studentService.getMyHomeworks(user.getId()),
                    studentService.getMyGroup(user.getId()),
                    null,
                    null
            );
        } else if (user.getRole() == Role.TEACHER) {
            return new HomePageDataResponse(
                    teacherService.getMyLessons(user.getId()),
                    teacherService.getHomeworksToCheck(user.getId()),
                    null,
                    teacherService.getMyStudents(user.getId()),
                    teacherService.getGroupsForTeacher(user.getId())
            );

        } else if (user.getRole() == Role.ADMIN) {
            throw new UnsupportedOperationException("Admin home page not implemented yet");
        } else {
            throw new IllegalStateException("Unknown role: " + user.getRole());
        }
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
    public List<GroupShortDto> getGroupsForTeacher(Long teacherId) {
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
    public List<UserProfileDtoResponse> getStudentsForTeacher(Long teacherId) {
        return teacherService.getMyStudents(teacherId);
    }
}
