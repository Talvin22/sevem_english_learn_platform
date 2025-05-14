package com.dzhaparov.controller.profile;

import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;

    public ProfileController(TeacherService teacherService, AuthHelper authHelper) {
        this.teacherService = teacherService;
        this.authHelper = authHelper;
    }

    @GetMapping("/students")
    public List<UserProfileDtoResponse> getMyStudents() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getMyStudents(teacherId);
    }

    @GetMapping("/groups")
    public List<GroupShortDto> getMyGroups() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getGroupsForTeacher(teacherId);
    }
}