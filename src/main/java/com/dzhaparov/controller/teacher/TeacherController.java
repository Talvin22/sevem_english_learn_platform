package com.dzhaparov.controller.teacher;

import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;
    private final UserRepository userRepository;

    public TeacherController(TeacherService teacherService, AuthHelper authHelper, UserRepository userRepository) {
        this.teacherService = teacherService;
        this.authHelper = authHelper;
        this.userRepository = userRepository;
    }

    @GetMapping("/students")
    public List<UserProfileDtoResponse> getMyStudents() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getMyStudents(teacherId);
    }
    @PostMapping("/assign-student")
    public ResponseEntity<Void> assignStudent(@RequestParam Long studentId) {
        Long teacherId = authHelper.getCurrentUser().getId();
        teacherService.assignStudentToTeacher(teacherId, studentId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/unassigned-students")
    public List<UserProfileDtoResponse> getUnassignedStudents() {
        return userRepository.findUnassignedStudents().stream()
                .map(user -> new UserProfileDtoResponse(
                        user.getId(),
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getRole(),
                        Collections.emptyList(),
                        user.getSalaryPerLesson()
                ))
                .toList();
    }

}