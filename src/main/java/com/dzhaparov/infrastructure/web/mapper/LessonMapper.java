package com.dzhaparov.infrastructure.web.mapper;

import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;
import com.dzhaparov.infrastructure.web.dto.homework.request.HomeworkBriefDto;
import com.dzhaparov.infrastructure.web.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.infrastructure.web.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoCreateResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonShortCardResponse;
import com.dzhaparov.infrastructure.web.dto.student.StudentAttendanceDto;
import com.dzhaparov.infrastructure.web.dto.user.response.UserDtoDetailResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LessonMapper {

    public LessonDtoCreateResponse toCreateResponse(Lesson lesson, List<Long> studentIds) {
        return LessonDtoCreateResponse.of(true, lesson, studentIds);
    }

    public LessonShortCardResponse toShortCard(Lesson lesson, List<String> studentNames) {
        return new LessonShortCardResponse(
                lesson.getId(),
                lesson.getDateUtc(),
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                studentNames,
                lesson.getStatus()
        );
    }

    public LessonEditDtoResponse toEditResponse(Lesson lesson,
                                                List<String> studentNames,
                                                List<StudentAttendanceDto> attendance,
                                                List<HomeworkBriefDto> homeworks) {
        return new LessonEditDtoResponse(
                lesson.getId(),
                lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                studentNames,
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                lesson.getDateUtc(),
                lesson.getStatus(),
                lesson.getCancelingReason(),
                lesson.getCancelledBy(),
                attendance,
                homeworks
        );
    }

    public LessonDtoCreateResponse fromRequest(CreateLessonRequest request, Lesson lesson, List<Long> studentIds) {
        lesson.setStatus(LessonStatus.PLANNED);
        return LessonDtoCreateResponse.of(true, lesson, studentIds);
    }

    public LessonShortCardResponse toShortCard(Lesson lesson, Map<Long, List<LessonParticipant>> participantsMap) {
        List<String> studentNames = participantsMap.getOrDefault(lesson.getId(), List.of()).stream()
                .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                .toList();

        return toShortCard(lesson, studentNames);
    }

    public List<UserDtoDetailResponse> toStudentDetails(List<? extends com.dzhaparov.entity.user.User> students) {
        return students.stream()
                .map(student -> new UserDtoDetailResponse(student.getId(), student.getFirst_name(), student.getLast_name(), student.getEmail()))
                .toList();
    }

    public LessonEditDtoResponse toEditResponse(Lesson lesson,
                                                List<LessonParticipant> participants,
                                                List<HomeworkBriefDto> homeworks) {
        List<String> studentNames = participants.stream()
                .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                .toList();

        List<StudentAttendanceDto> studentAttendanceList = participants.stream()
                .map(p -> new StudentAttendanceDto(
                        p.getStudent().getId(),
                        p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name(),
                        p.getAttendanceStatus()
                )).toList();

        return toEditResponse(lesson, studentNames, studentAttendanceList, homeworks);
    }

    public LessonDtoDetailResponse toDetailResponse(LessonParticipant participant) {
        Lesson lesson = participant.getLesson();
        String studentFullName = participant.getStudent().getFirst_name() + " " + participant.getStudent().getLast_name();

        return new LessonDtoDetailResponse(
                lesson.getId(),
                lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                List.of(studentFullName),
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                lesson.getDateUtc(),
                lesson.getStatus(),
                lesson.getCancelingReason(),
                participant.getAttendanceStatus(),
                lesson.getCancelledBy()
        );
    }

    public LessonShortCardResponse toShortCard(LessonParticipant participant) {
        Lesson lesson = participant.getLesson();
        String studentName = participant.getStudent().getFirst_name() + " " + participant.getStudent().getLast_name();
        return new LessonShortCardResponse(
                lesson.getId(),
                lesson.getDateUtc(),
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                List.of(studentName),
                lesson.getStatus()
        );
    }

    public LessonShortCardResponse toShortCard(Lesson lesson, String groupName, List<String> studentNames) {
        return new LessonShortCardResponse(
                lesson.getId(),
                lesson.getDateUtc(),
                groupName,
                studentNames,
                lesson.getStatus()
        );
    }

    public UpdateLessonStatusRequest.ParticipantUpdate toParticipantUpdate(LessonParticipant participant) {
        LessonAttendanceStatus status = participant.getAttendanceStatus();
        return new UpdateLessonStatusRequest.ParticipantUpdate(participant.getStudent().getId(), status);
    }
}
