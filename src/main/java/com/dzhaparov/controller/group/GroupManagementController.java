package com.dzhaparov.controller.group;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupManagementController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;

    public GroupManagementController(TeacherService teacherService, AuthHelper authHelper) {
        this.teacherService = teacherService;
        this.authHelper = authHelper;
    }

    @GetMapping("/teacher")
    public List<GroupDtoResponse> getGroups() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getGroupsForTeacher(teacherId);
    }

    @GetMapping("/{groupId}/students")
    public List<UserProfileDtoResponse> getStudentsInGroup(@PathVariable Long groupId) {
        return teacherService.getMyStudents(authHelper.getCurrentUser().getId()).stream()
                .filter(s -> s.group() != null && s.group().getId().equals(groupId))
                .toList();
    }

    @PostMapping("/{groupId}/add-student")
    public GroupDtoResponse addStudent(@PathVariable Long groupId, @RequestBody AddStudentRequest request) {
        return teacherService.addStudentToGroup(groupId, request.studentId());
    }

    @DeleteMapping("/{groupId}/remove-student")
    public void removeStudent(@PathVariable Long groupId, @RequestParam Long studentId) {
        teacherService.removeStudentFromGroup(groupId, studentId);
    }

    @GetMapping("/students/free")
    public List<UserProfileDtoResponse> getFreeStudents() {
        return teacherService.getMyStudents(authHelper.getCurrentUser().getId()).stream()
                .filter(s -> s.group() == null)
                .toList();
    }

    public record AddStudentRequest(Long studentId) {}
}