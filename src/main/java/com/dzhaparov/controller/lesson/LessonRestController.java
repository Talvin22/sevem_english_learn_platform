package com.dzhaparov.controller.lesson;

import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.dto.lesson.response.LessonShortCardResponse;
import com.dzhaparov.service.lesson.LessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/lesson/update")
    public void updateLesson(@RequestBody UpdateLessonStatusRequest request) {
        lessonService.updateLessonStatus(request);
    }

    @GetMapping("/week")
    public List<LessonShortCardResponse> getLessonsForWeek(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {
        ZonedDateTime start = ZonedDateTime.parse(startStr);
        ZonedDateTime end = ZonedDateTime.parse(endStr);
        return lessonService.getLessonsForWeek(start, end);
    }

    @GetMapping("/weekly")
    public Map<String, List<LessonShortCardResponse>> getLessonsByWeek(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("timeZone") String timeZone,
            Authentication authentication) {

        String email = authentication.getName();
        return lessonService.getLessonsByWeek(startDate, timeZone, email);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/lesson/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return ResponseEntity.noContent().build();
    }
}