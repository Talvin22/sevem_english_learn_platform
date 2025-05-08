package com.dzhaparov.dto.lesson.request;

import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

public record LessonParticipantInfo(
        Long studentId,
        String studentName,
        LessonAttendanceStatus attendanceStatus
) {}