package com.dzhaparov.repository.homework;

import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByStudentId(Long studentId);
    List<Homework> findByGroupId(Long groupId);
    List<Homework> findByLessonTeacherId(Long teacherId);
    @Query("SELECT h FROM Homework h WHERE h.lesson.teacher.id = :teacherId AND h.status = :status")
    List<Homework> findByLessonTeacherIdAndStatus(Long teacherId, HomeworkStatus status);
}
