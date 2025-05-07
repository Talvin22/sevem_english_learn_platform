package com.dzhaparov.repository.lesson;


import com.dzhaparov.entity.lesson.LessonParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonParticipantRepository extends JpaRepository<LessonParticipant, Long> {
}
