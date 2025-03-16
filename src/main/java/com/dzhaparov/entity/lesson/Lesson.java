package com.dzhaparov.entity.lesson;

import com.dzhaparov.entity.user.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "date_utc", nullable = false)
    private ZonedDateTime dateUtc;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LessonStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceling_reason")
    private CancelingReasons cancelingReason;


    public ZonedDateTime getDateForTimeZone(String timeZone) {
        return dateUtc.withZoneSameInstant(ZoneId.of(timeZone));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public ZonedDateTime getDateUtc() {
        return dateUtc;
    }

    public void setDateUtc(ZonedDateTime dateUtc) {
        this.dateUtc = dateUtc;
    }

    public LessonStatus getStatus() {
        return status;
    }

    public void setStatus(LessonStatus status) {
        this.status = status;
    }

    public CancelingReasons getCancelingReason() {
        return cancelingReason;
    }

    public void setCancelingReason(CancelingReasons cancelingReason) {
        this.cancelingReason = cancelingReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(teacher, lesson.teacher) && Objects.equals(student, lesson.student) && Objects.equals(dateUtc, lesson.dateUtc) && status == lesson.status && cancelingReason == lesson.cancelingReason;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacher, student, dateUtc, status, cancelingReason);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", student=" + student +
                ", dateUtc=" + dateUtc +
                ", status=" + status +
                ", cancelingReason=" + cancelingReason +
                '}';
    }
}