package com.dzhaparov.infrastructure.web.dto.lesson.request;

import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

public record LessonParticipantInfo(
        Long studentId,
        String studentName,
        LessonAttendanceStatus attendanceStatus
) {}