package com.dzhaparov.controller.group;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/groups")
@RequiredArgsConstructor
public class GroupManagementController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;

    @GetMapping
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

    @PostMapping("/{groupId}/students/{studentId}")
    public GroupDtoResponse addStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
        return teacherService.addStudentToGroup(groupId, studentId);
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public void removeStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
        teacherService.removeStudentFromGroup(groupId, studentId);
    }
}
