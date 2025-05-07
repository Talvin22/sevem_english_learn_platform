package com.dzhaparov.repository.lesson;


import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonParticipantRepository extends JpaRepository<LessonParticipant, Long> {
    List<LessonParticipant> findByLessonId(Long lessonId);
    List<LessonParticipant> findByStudentId(Long studentId);
    List<LessonParticipant> findAllByLessonIn(List<Lesson> lessons);
    List<LessonParticipant> findAllByStudentId(Long studentId);
}
