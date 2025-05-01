package com.dzhaparov.repository.user;

import com.dzhaparov.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByGroup_Teacher_Id(Long teacherId);
    List<User> findAllByTeacherId(Long teacherId);
}
