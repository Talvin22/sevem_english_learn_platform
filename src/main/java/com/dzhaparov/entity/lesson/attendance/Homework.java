package com.dzhaparov.entity.lesson.attendance;

import com.dzhaparov.entity.homework.HomeworkStatus;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "homeworks")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HomeworkStatus status = HomeworkStatus.NOT_SUBMITTED;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public HomeworkStatus getStatus() {
        return status;
    }

    public void setStatus(HomeworkStatus status) {
        this.status = status;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return Objects.equals(id, homework.id) && Objects.equals(lesson, homework.lesson) && Objects.equals(student, homework.student) && status == homework.status && Objects.equals(grade, homework.grade) && Objects.equals(content, homework.content) && Objects.equals(fileUrl, homework.fileUrl) && Objects.equals(submittedAt, homework.submittedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lesson, student, status, grade, content, fileUrl, submittedAt);
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", lesson=" + lesson +
                ", student=" + student +
                ", status=" + status +
                ", grade=" + grade +
                ", content='" + content + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
