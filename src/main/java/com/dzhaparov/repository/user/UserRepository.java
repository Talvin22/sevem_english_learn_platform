package com.dzhaparov.repository.user;

import com.dzhaparov.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByTeacherId(Long teacherId);
    @Query("SELECT u FROM User u JOIN u.groups g WHERE g.name = ?1")
    List<User> findAllByGroupName(String groupName);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.teacher WHERE u.role = 'STUDENT'")
    List<User> findAllStudentsWithTeachers();
    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT' AND u.teacher.id = :teacherId")
    List<User> findStudentsByTeacherId(@Param("teacherId") Long teacherId);
}
