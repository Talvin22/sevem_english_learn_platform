package com.dzhaparov.controller.teacher;

import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;

    public TeacherController(TeacherService teacherService, AuthHelper authHelper) {
        this.teacherService = teacherService;
        this.authHelper = authHelper;
    }

    @GetMapping("/students")
    public List<UserProfileDtoResponse> getMyStudents() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getMyStudents(teacherId);
    }

}