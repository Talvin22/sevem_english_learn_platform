package com.dzhaparov.controller.lesson;


import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.dto.user.response.UserDtoDetailResponse;
import com.dzhaparov.service.lesson.LessonService;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/create")
    public String showCreateLessonForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<UserDtoDetailResponse> students = lessonService.getAllStudentsForTeacher(email);
        System.out.println("List" + students);

        model.addAttribute("students", students);
        model.addAttribute("lessonRequest", new CreateLessonRequest());

        return "lesson/create-lesson";
    }

    @PostMapping("/create")
    public String createLesson(@ModelAttribute @Valid CreateLessonRequest request,
                               Authentication authentication) {
        lessonService.createLesson(request, authentication.getName());
        return "redirect:/";
    }
    @GetMapping("/edit")
    public String showEditLessonForm(@RequestParam("id") Long lessonId, Model model) {
        LessonEditDtoResponse lesson = lessonService.getLessonForEdit(lessonId);
        model.addAttribute("lesson", lesson);
        model.addAttribute("updateRequest", new UpdateLessonStatusRequest(
                lesson.id(), lesson.status(), lesson.cancelingReason(), lesson.cancelledBy(), lesson.attendanceStatus()
        ));
        return "lesson/edit-lesson";
    }

    @PostMapping("/update")
    public String updateLesson(@ModelAttribute @Valid UpdateLessonStatusRequest request) {
        lessonService.updateLessonStatus(request);
        return "redirect:/";
    }
}
