package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

import java.time.ZonedDateTime;

public record LessonEditDtoResponse(
        Long id,
        String teacherName,
        String studentName,
        String groupName,
        ZonedDateTime dateUtc,
        LessonStatus status,
        LessonAttendanceStatus attendanceStatus,
        CancelingReasons cancelingReason,
        CancelledBy cancelledBy
) {
}