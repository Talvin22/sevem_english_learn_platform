package com.dzhaparov.controller.lesson;


import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.service.lesson.LessonService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/create")
    public String showCreateLessonForm(Model model, Authentication authentication) {
        model.addAttribute("createLessonRequest", new CreateLessonRequest());
        model.addAttribute("students", lessonService.getAllStudentsForTeacher(authentication.getName()));
        return "lesson/create-lesson";
    }

    @PostMapping("/create")
    public String createLesson(@ModelAttribute @Valid CreateLessonRequest request,
                               Authentication authentication) {
        lessonService.createLesson(request, authentication.getName());
        return "redirect:/";
    }
}
