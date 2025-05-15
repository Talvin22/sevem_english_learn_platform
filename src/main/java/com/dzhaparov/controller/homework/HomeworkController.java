package com.dzhaparov.controller.homework;

import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.repository.homework.HomeworkRepository;
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
    private final HomeworkRepository homeworkRepository;

    public HomeworkController(HomeworkService homeworkService, AuthHelper authHelper, HomeworkRepository homeworkRepository) {
        this.homeworkService = homeworkService;
        this.authHelper = authHelper;
        this.homeworkRepository = homeworkRepository;
    }

    @PostMapping
    public ResponseEntity<List<HomeworkDtoResponse>> createHomework(@RequestBody CreateHomeworkRequest request) {
        Long teacherId = authHelper.getCurrentUser().getId();
        List<HomeworkDtoResponse> response = homeworkService.createHomework(request, teacherId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HomeworkDtoResponse> getHomeworkById(@PathVariable Long id) {
        Homework hw = homeworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        return ResponseEntity.ok(HomeworkDtoResponse.from(hw));
    }
}
