package com.dzhaparov.controller.group;

import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupManagementController {

    private final TeacherService teacherService;
    private final AuthHelper authHelper;
    private final GroupRepository groupRepository;

    public GroupManagementController(TeacherService teacherService, AuthHelper authHelper, GroupRepository groupRepository) {
        this.teacherService = teacherService;
        this.authHelper = authHelper;
        this.groupRepository = groupRepository;
    }


    @GetMapping("/teacher")
    public List<GroupDtoResponse> getGroups() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getGroupsForTeacher(teacherId);
    }

    @GetMapping("/{groupId}/students")
    public List<UserProfileDtoResponse> getStudentsInGroup(@PathVariable Long groupId) {
        var group = groupRepository.findById(groupId).orElseThrow();
        return group.getStudents().stream()
                .map(user -> new UserProfileDtoResponse(
                        user.getId(),
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getRole(),
                        user.getGroups().stream()
                                .map(g -> new GroupShortDto(g.getId(), g.getName()))
                                .toList(),
                        user.getSalaryPerLesson()
                ))
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
                .filter(s -> s.groups().isEmpty())
                .toList();
    }

    public record AddStudentRequest(Long studentId) {}
}