package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

import java.util.List;

public record UpdateLessonStatusRequest(
        Long lessonId,
        LessonStatus status,
        CancelingReasons cancelingReason,
        CancelledBy cancelledBy,
        List<ParticipantUpdate> participants
) {
    public record ParticipantUpdate(Long studentId, LessonAttendanceStatus attendanceStatus) {}
}