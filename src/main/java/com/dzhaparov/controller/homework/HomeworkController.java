package com.dzhaparov.controller.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.service.homework.HomeworkService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    private final HomeworkService homeworkService;
    private final AuthHelper authHelper;

    public HomeworkController(HomeworkService homeworkService, AuthHelper authHelper) {
        this.homeworkService = homeworkService;
        this.authHelper = authHelper;
    }

    @PostMapping
    public ResponseEntity<List<HomeworkDtoResponse>> createHomework(@RequestBody CreateHomeworkRequest request) {
        Long teacherId = authHelper.getCurrentUser().getId();
        List<HomeworkDtoResponse> response = homeworkService.createHomework(request, teacherId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeworkDtoResponse> getHomeworkById(@PathVariable Long id) {
        var user = authHelper.getCurrentUser();
        return ResponseEntity.ok(homeworkService.getHomeworkById(id, user));
    }
    @GetMapping("/by-lesson/{lessonId}")
    public ResponseEntity<HomeworkDtoListResponse> getHomeworksByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(homeworkService.getHomeworksByLessonId(lessonId));
    }
}
