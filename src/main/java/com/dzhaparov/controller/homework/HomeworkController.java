package com.dzhaparov.controller.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.service.homework.HomeworkService;
import com.dzhaparov.service.teacher.TeacherService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    private final HomeworkService homeworkService;
    private final AuthHelper authHelper;
    private final TeacherService teacherService;

    public HomeworkController(HomeworkService homeworkService, AuthHelper authHelper, TeacherService teacherService) {
        this.homeworkService = homeworkService;
        this.authHelper = authHelper;
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<List<HomeworkDtoResponse>> createHomework(@RequestBody CreateHomeworkRequest request) {
        Long teacherId = authHelper.getCurrentUser().getId();
        List<HomeworkDtoResponse> response = homeworkService.createHomework(request, teacherId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeworkDtoDetailResponse> getHomeworkById(@PathVariable Long id) {
        var user = authHelper.getCurrentUser();
        return ResponseEntity.ok(homeworkService.getHomeworkById(id, user));
    }
    @GetMapping("/by-lesson/{lessonId}")
    public ResponseEntity<HomeworkDtoListResponse> getHomeworksByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(homeworkService.getHomeworksByLessonId(lessonId));
    }
//    @PostMapping("/grade")
//    public ResponseEntity<HomeworkDtoGradeResponse> updateHomework(@RequestBody HomeworkDtoGradeRequest request) {
//        HomeworkDtoGradeResponse response = teacherService.updateHomeworkAsTeacher(request, authHelper.getCurrentUser().getId());
//        return ResponseEntity.ok(response);
//    }
    @PostMapping("/submit")
    public ResponseEntity<?> submitHomework(@RequestBody Map<String, Long> payload) {
        Long homeworkId = payload.get("homeworkId");
        homeworkService.submitHomework(homeworkId, authHelper.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/grade")
    public ResponseEntity<HomeworkDtoGradeResponse> updateHomework(
            @PathVariable Long id,
            @RequestBody HomeworkDtoGradeRequest request) {

        if (!id.equals(request.homeworkId())) {
            return ResponseEntity.badRequest().build();
        }

        HomeworkDtoGradeResponse response = teacherService.updateHomeworkAsTeacher(
                request, authHelper.getCurrentUser().getId());

        return ResponseEntity.ok(response);
    }
}
