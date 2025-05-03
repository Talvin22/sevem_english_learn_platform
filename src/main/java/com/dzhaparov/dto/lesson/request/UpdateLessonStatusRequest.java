package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

public record UpdateLessonStatusRequest(
        Long lessonId,
        LessonStatus status,
        CancelingReasons cancelingReason,
        CancelledBy cancelledBy,
        LessonAttendanceStatus attendanceStatus
) {
}