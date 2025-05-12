package com.dzhaparov.entity.group;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
            name = "group_students",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Lesson> lessons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(isActive, group.isActive) && Objects.equals(teacher, group.teacher) && Objects.equals(students, group.students) && Objects.equals(lessons, group.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive, teacher, students, lessons);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", teacher=" + teacher +
                ", students=" + students +
                ", lessons=" + lessons +
                '}';
    }
}