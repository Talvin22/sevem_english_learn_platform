package com.dzhaparov.repository.lesson;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByTeacherId(Long teacherId);
    List<Lesson> findByTeacherIdAndDateUtcBetweenOrderByDateUtcAsc(Long teacherId, ZonedDateTime start, ZonedDateTime end);
    List<Lesson> findAllByTeacherAndDateUtcBetween(User teacher, ZonedDateTime start, ZonedDateTime end);
    @Modifying
    @Query("DELETE FROM LessonParticipant lp WHERE lp.id.lessonId = :lessonId")
    void deleteAllByLessonId(@Param("lessonId") Long lessonId);
}
