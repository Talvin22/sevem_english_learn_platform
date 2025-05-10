package com.dzhaparov.dto.lesson.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateLessonRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateUtc;

    private Long studentId;
    private String groupName;
    private String timeZone;
    private Integer repeatWeeks;

    public LocalDateTime getDateUtc() {
        return dateUtc;
    }

    public void setDateUtc(LocalDateTime dateUtc) {
        this.dateUtc = dateUtc;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getRepeatWeeks() {
        return repeatWeeks;
    }

    public void setRepeatWeeks(Integer repeatWeeks) {
        this.repeatWeeks = repeatWeeks;
    }
}