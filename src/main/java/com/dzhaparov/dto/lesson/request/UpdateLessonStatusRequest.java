package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;

public class UpdateLessonStatusRequest {
    private Long lessonId;
    private LessonStatus status;
    private CancelingReasons cancelingReason;

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
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
}