package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

import java.time.ZonedDateTime;

public record LessonDtoDetailResponse(
        Long id,
        String teacherFullName,
        String studentFullName,
        String groupName,
        ZonedDateTime dateUtc,
        LessonStatus status,
        CancelingReasons cancelingReason,
        LessonAttendanceStatus attendanceStatus,
        com.dzhaparov.entity.lesson.attendance.CancelledBy cancelledBy
) {}