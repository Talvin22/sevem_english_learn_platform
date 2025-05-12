package com.dzhaparov.repository.lesson;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.entity.lesson.LessonParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonParticipantRepository extends JpaRepository<LessonParticipant, LessonParticipantId> {

    List<LessonParticipant> findAllByLessonId(Long lessonId);

    List<LessonParticipant> findAllByStudentId(Long studentId);

    List<LessonParticipant> findAllByLessonIn(List<Lesson> lessons);
    void deleteAllByLessonId(Long lessonId);
}