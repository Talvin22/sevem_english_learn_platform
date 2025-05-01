package com.dzhaparov.service.lesson;

import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public LessonService(LessonRepository lessonRepository, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    public void createLesson(CreateLessonRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setDateUtc(ZonedDateTime.from(LocalDateTime.parse(request.getDateTime())));
        lesson.setStatus("PLANNED");

        lessonRepository.save(lesson);
    }
}