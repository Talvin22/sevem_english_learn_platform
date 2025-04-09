package com.dzhaparov.entity.user;

import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.role.Role;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String hashed_password;
    @Column(nullable = true)
    private Double salaryPerLesson;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public boolean isTeacher() {
        return this.role == Role.TEACHER;
    }

    public boolean isStudent() {
        return this.role == Role.STUDENT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public Double getSalaryPerLesson() {
        return salaryPerLesson;
    }

    public void setSalaryPerLesson(Double salaryPerLesson) {
        this.salaryPerLesson = salaryPerLesson;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && role == user.role && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(email, user.email) && Objects.equals(hashed_password, user.hashed_password) && Objects.equals(salaryPerLesson, user.salaryPerLesson) && Objects.equals(group, user.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, first_name, last_name, email, hashed_password, salaryPerLesson, group);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", hashed_password='" + hashed_password + '\'' +
                ", salaryPerLesson=" + salaryPerLesson +
                ", group=" + group +
                '}';
    }
}
