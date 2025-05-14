package com.dzhaparov.controller.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.service.homework.HomeworkService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<HomeworkDtoResponse> createHomework(@RequestBody CreateHomeworkRequest request) {
        Long teacherId = authHelper.getCurrentUser().getId();
        HomeworkDtoResponse response = homeworkService.createHomework(request, teacherId);
        return ResponseEntity.ok(response);
    }
}
