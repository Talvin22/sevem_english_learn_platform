package com.dzhaparov.repository.lesson;


import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonParticipantRepository extends JpaRepository<LessonParticipant, Long> {
    List<LessonParticipant> findByLessonId(Long lessonId);
    List<LessonParticipant> findByStudentId(Long studentId);
    List<LessonParticipant> findAllByLessonIn(List<Lesson> lessons);
    List<LessonParticipant> findAllByStudentId(Long studentId);
    List<LessonParticipant> findByLesson(Lesson lesson);
    List<LessonParticipant> findAllByLesson(Lesson lesson);
    @Modifying
    @Transactional
    @Query("DELETE FROM LessonParticipant lp WHERE lp.id.lessonId = :lessonId")
    void deleteAllByLessonId(@Param("lessonId") Long lessonId);
}
