package com.dzhaparov.controller.lesson;

import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.service.lesson.LessonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons/api")
public class LessonRestController {

    private final LessonService lessonService;

    public LessonRestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lesson")
    public LessonEditDtoResponse getLessonForEdit(@RequestParam("id") Long lessonId) {
        return lessonService.getLessonForEdit(lessonId);
    }

    @PostMapping("/lesson/update")
    public void updateLesson(@RequestBody UpdateLessonStatusRequest request) {
        lessonService.updateLessonStatus(request);
    }
}