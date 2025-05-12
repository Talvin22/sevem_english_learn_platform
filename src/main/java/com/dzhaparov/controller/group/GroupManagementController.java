package com.dzhaparov.controller.group;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupManagementController {

    private final TeacherService teacherService;
    private final GroupRepository groupRepository;
    private final AuthHelper authHelper;

    public GroupManagementController(TeacherService teacherService,
                                     GroupRepository groupRepository,
                                     AuthHelper authHelper) {
        this.teacherService = teacherService;
        this.groupRepository = groupRepository;
        this.authHelper = authHelper;
    }

    @GetMapping("/teacher")
    public List<GroupDtoResponse> getGroups() {
        Long teacherId = authHelper.getCurrentUser().getId();
        return teacherService.getGroupsForTeacher(teacherId);
    }

    @GetMapping("/{groupId}/students")
    public List<UserProfileDtoResponse> getStudentsInGroup(@PathVariable Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return group.getStudents().stream()
                .map(s -> new UserProfileDtoResponse(
                        s.getId(),
                        s.getFirst_name(),
                        s.getLast_name(),
                        s.getEmail(),
                        s.getRole(),
                        null,
                        s.getSalaryPerLesson()
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
        return teacherService.getStudentsWithoutGroups();
    }

    public record AddStudentRequest(Long studentId) {}
}