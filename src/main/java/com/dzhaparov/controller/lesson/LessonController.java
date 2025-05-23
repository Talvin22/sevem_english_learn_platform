package com.dzhaparov.controller.lesson;


import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.dto.user.response.UserDtoDetailResponse;
import com.dzhaparov.service.lesson.LessonService;
import com.dzhaparov.util.AuthHelper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final AuthHelper authHelper;

    public LessonController(LessonService lessonService, AuthHelper authHelper) {
        this.lessonService = lessonService;
        this.authHelper = authHelper;
    }
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/create")
    public String showCreateLessonForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<UserDtoDetailResponse> students = lessonService.getAllStudentsForTeacher(email);
        System.out.println("List" + students);

        model.addAttribute("students", students);
        model.addAttribute("lessonRequest", new CreateLessonRequest());

        return "lesson/create-lesson";
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public String createLesson(@ModelAttribute @Valid CreateLessonRequest request,
                               Authentication auth) {
        lessonService.createLesson(request, auth.getName());
        return "redirect:/";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/edit")
    public String showEditLessonForm(@RequestParam("id") Long lessonId, Model model) {
        LessonEditDtoResponse lesson = lessonService.getLessonForEdit(lessonId);

        UpdateLessonStatusRequest updateRequest = new UpdateLessonStatusRequest(
                lesson.id(),
                lesson.status(),
                lesson.cancelingReason(),
                lesson.cancelledBy(),
                lesson.participants().stream()
                        .map(p -> new UpdateLessonStatusRequest.ParticipantUpdate(p.studentId(), p.attendanceStatus()))
                        .toList()
        );

        model.addAttribute("lesson", lesson);
        model.addAttribute("updateRequest", updateRequest);
        return "lesson/edit-lesson";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/update")
    public String updateLesson(@ModelAttribute @Valid UpdateLessonStatusRequest request) {
        lessonService.updateLessonStatus(request);
        return "redirect:/";
    }
}
