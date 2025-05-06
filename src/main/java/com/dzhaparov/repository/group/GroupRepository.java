package com.dzhaparov.repository.group;

import com.dzhaparov.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByTeacherId(Long teacherId);

    Optional<Group> findByName(String groupName);
    @Query("SELECT g FROM Group g WHERE g.name = :name AND g.teacher.email = :email")
    Optional<Group> findByNameAndTeacherEmail(@Param("name") String name, @Param("email") String email);
}