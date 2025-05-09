package com.dzhaparov.service.lesson;

import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonDtoCreateResponse;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.dto.lesson.response.LessonShortCardResponse;
import com.dzhaparov.dto.student.StudentAttendanceDto;
import com.dzhaparov.dto.user.response.UserDtoDetailResponse;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.util.AuthHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final AuthHelper authHelper;
    private final LessonParticipantRepository lessonParticipantRepository;

    public LessonService(LessonRepository lessonRepository, UserRepository userRepository, GroupRepository groupRepository, AuthHelper authHelper, LessonParticipantRepository lessonParticipantRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.authHelper = authHelper;
        this.lessonParticipantRepository = lessonParticipantRepository;

    }

    @Transactional
    public LessonDtoCreateResponse createLesson(CreateLessonRequest request, String email) {
        User teacher = authHelper.getCurrentUser();

        LocalDateTime localDateTime = request.getDateUtc();
        String timeZone = request.getTimeZone();
        ZonedDateTime utcDate = localDateTime
                .atZone(ZoneId.of(timeZone))
                .withZoneSameInstant(ZoneId.of("UTC"));

        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setDateUtc(utcDate);
        lesson.setStatus(LessonStatus.PLANNED);

        List<Long> studentIds = new ArrayList<>();

        if (request.getGroupName() != null && !request.getGroupName().isBlank()) {
            Group group = groupRepository.findByNameAndTeacherEmail(request.getGroupName(), email)
                    .orElseThrow(() -> new IllegalArgumentException("Group not found: " + request.getGroupName()));

            lesson.setGroup(group);
            lessonRepository.save(lesson);

            for (User student : group.getStudents()) {
                LessonParticipant participant = new LessonParticipant();
                participant.setLesson(lesson);
                participant.setStudent(student);
                participant.setAttendanceStatus(LessonAttendanceStatus.PLANNED);
                lessonParticipantRepository.save(participant);
                studentIds.add(student.getId());
            }
        } else if (request.getStudentId() != null) {
            User student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            lessonRepository.save(lesson);

            LessonParticipant participant = new LessonParticipant();
            participant.setLesson(lesson);
            participant.setStudent(student);
            participant.setAttendanceStatus(LessonAttendanceStatus.PLANNED);
            lessonParticipantRepository.save(participant);
            studentIds.add(student.getId());
        } else {
            throw new IllegalArgumentException("Either group name or student must be specified");
        }

        return LessonDtoCreateResponse.of(true, lesson, studentIds);
    }


    public List<UserDtoDetailResponse> getAllStudentsForTeacher(String email) {
        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<User> students = userRepository.findAllByTeacherId(teacher.getId());

        return students.stream()
                .map(student -> new UserDtoDetailResponse(student.getId(), student.getFirst_name(), student.getLast_name(), student.getEmail()))
                .toList();
    }

    @Transactional
    public void updateLessonStatus(UpdateLessonStatusRequest request) {
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setStatus(request.status());

        if (request.status() == LessonStatus.CANCELLED) {
            if (request.cancelledBy() == null || request.cancelingReason() == null) {
                throw new IllegalArgumentException("Cancel reason and who cancelled must be provided for cancelled lessons.");
            }
            lesson.setCancelledBy(request.cancelledBy());
            lesson.setCancelingReason(request.cancelingReason());
        } else {
            lesson.setCancelledBy(null);
            lesson.setCancelingReason(null);
        }

        List<LessonParticipant> participants = lessonParticipantRepository.findAllByLesson(lesson);
        Map<Long, LessonAttendanceStatus> statusMap = request.participants().stream()
                .collect(Collectors.toMap(UpdateLessonStatusRequest.ParticipantUpdate::studentId,
                        UpdateLessonStatusRequest.ParticipantUpdate::attendanceStatus));

        for (LessonParticipant participant : participants) {
            if (statusMap.containsKey(participant.getStudent().getId())) {
                participant.setAttendanceStatus(statusMap.get(participant.getStudent().getId()));
            }
        }

        lessonRepository.save(lesson);
        lessonParticipantRepository.saveAll(participants);
    }

    public LessonEditDtoResponse getLessonForEdit(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<LessonParticipant> participants = lessonParticipantRepository.findAllByLesson(lesson);

        List<String> studentNames = participants.stream()
                .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                .toList();

        List<StudentAttendanceDto> studentAttendanceList = participants.stream()
                .map(p -> new StudentAttendanceDto(
                        p.getStudent().getId(),
                        p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name(),
                        p.getAttendanceStatus()
                )).toList();

        return new LessonEditDtoResponse(
                lesson.getId(),
                lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                studentNames,
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                lesson.getDateUtc(),
                lesson.getStatus(),
                lesson.getCancelingReason(),
                lesson.getCancelledBy(),
                studentAttendanceList
        );
    }
    public List<Lesson> getLessonsBetween(ZonedDateTime start, ZonedDateTime end, Long teacherId) {
        return lessonRepository.findByTeacherIdAndDateUtcBetweenOrderByDateUtcAsc(teacherId, start, end);
    }
    public List<LessonShortCardResponse> getLessonsForWeek(ZonedDateTime start, ZonedDateTime end) {
        User teacher = authHelper.getCurrentUser();

        List<Lesson> lessons = lessonRepository.findAllByTeacherAndDateUtcBetween(teacher, start, end);

        return lessons.stream()
                .map(lesson -> new LessonShortCardResponse(
                        lesson.getId(),
                        lesson.getDateUtc(),
                        lesson.getStatus(),
                        lesson.getGroup() != null ? lesson.getGroup().getName() : null
                ))
                .toList();
    }
}