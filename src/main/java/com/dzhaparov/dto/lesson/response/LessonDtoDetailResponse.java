package com.dzhaparov.dto.lesson.response;

import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record LessonDtoDetailResponse(
        Long id,
        String teacherFullName,
        List<String> studentNames,
        String groupName,
        ZonedDateTime dateUtc,
        LessonStatus status,
        CancelingReasons cancelingReason,
        LessonAttendanceStatus attendanceStatus,
        CancelledBy cancelledBy
) {}