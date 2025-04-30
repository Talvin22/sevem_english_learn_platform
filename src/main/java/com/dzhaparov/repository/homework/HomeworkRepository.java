package com.dzhaparov.repository.homework;

import com.dzhaparov.entity.homework.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByStudentId(Long studentId);
    List<Homework> findByGroupId(Long groupId);
    List<Homework> findByLesson_Teacher_Id(Long teacherId);
}
