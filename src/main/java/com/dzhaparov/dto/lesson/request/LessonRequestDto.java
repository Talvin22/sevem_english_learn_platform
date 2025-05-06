package com.dzhaparov.dto.lesson.request;

import java.time.LocalDateTime;

public class LessonRequestDto {
    private Long studentId;
    private String groupName;
    private LocalDateTime dateUtc;

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

    public LocalDateTime getDateUtc() {
        return dateUtc;
    }

    public void setDateUtc(LocalDateTime dateUtc) {
        this.dateUtc = dateUtc;
    }
}
