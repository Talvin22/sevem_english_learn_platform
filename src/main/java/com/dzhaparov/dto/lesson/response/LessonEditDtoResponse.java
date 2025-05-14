package com.dzhaparov.dto.lesson.response;
import com.dzhaparov.dto.homework.request.HomeworkBriefDto;
import com.dzhaparov.dto.student.StudentAttendanceDto;
import com.dzhaparov.entity.lesson.CancelingReasons;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.CancelledBy;

import java.time.ZonedDateTime;
import java.util.List;

public record LessonEditDtoResponse(
        Long id,
        String teacherName,
        List<String> studentNames,
        String groupName,
        ZonedDateTime dateUtc,
        LessonStatus status,
        CancelingReasons cancelingReason,
        CancelledBy cancelledBy,
        List<StudentAttendanceDto> participants,
        List<HomeworkBriefDto> homeworks
) {}