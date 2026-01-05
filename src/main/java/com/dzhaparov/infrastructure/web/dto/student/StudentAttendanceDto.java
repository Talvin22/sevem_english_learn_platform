package com.dzhaparov.infrastructure.web.dto.student;

import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

public record StudentAttendanceDto(
        Long studentId,
        String name,
        LessonAttendanceStatus attendanceStatus
) {}
