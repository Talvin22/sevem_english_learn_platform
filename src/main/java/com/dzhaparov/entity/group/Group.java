package com.dzhaparov.entity.group;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "group")
    private List<User> students;

    @OneToMany(mappedBy = "group")
    private List<Lesson> lessons;

    // --- Геттеры ---
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public User getTeacher() {
        return teacher;
    }

    public List<User> getStudents() {
        return students;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    // --- Сеттеры ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    // --- equals / hashCode / toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", teacher=" + (teacher != null ? teacher.getId() : null) +
                '}';
    }
}