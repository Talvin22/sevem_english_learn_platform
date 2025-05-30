package com.dzhaparov.entity.lesson;

import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;
import com.dzhaparov.entity.user.User;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonParticipant> participants = new ArrayList<>();

    @Column(name = "date_utc", nullable = false)
    private ZonedDateTime dateUtc;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LessonStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceling_reason")
    private CancelingReasons cancelingReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status")
    private LessonAttendanceStatus attendanceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_by")
    private CancelledBy cancelledBy;

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

    public List<LessonParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<LessonParticipant> participants) {
        this.participants = participants;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LessonAttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(LessonAttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public CancelledBy getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(CancelledBy cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(teacher, lesson.teacher) && Objects.equals(participants, lesson.participants) && Objects.equals(dateUtc, lesson.dateUtc) && status == lesson.status && cancelingReason == lesson.cancelingReason && Objects.equals(group, lesson.group) && attendanceStatus == lesson.attendanceStatus && cancelledBy == lesson.cancelledBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacher, participants, dateUtc, status, cancelingReason, group, attendanceStatus, cancelledBy);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", participants=" + participants +
                ", dateUtc=" + dateUtc +
                ", status=" + status +
                ", cancelingReason=" + cancelingReason +
                ", group=" + group +
                ", attendanceStatus=" + attendanceStatus +
                ", cancelledBy=" + cancelledBy +
                '}';
    }
}