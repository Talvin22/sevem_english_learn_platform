package com.dzhaparov.service.lesson;

import java.util.*;

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
import com.dzhaparov.entity.role.Role;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.util.AuthHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

        List<User> students;
        if (request.getGroupName() != null && !request.getGroupName().isBlank()) {
            Group group = groupRepository.findByNameAndTeacherEmail(request.getGroupName(), email)
                    .orElseThrow(() -> new IllegalArgumentException("Group not found: " + request.getGroupName()));
            lesson.setGroup(group);
            students = group.getStudents();
        } else if (request.getStudentId() != null) {
            User student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            students = List.of(student);
        } else {
            throw new IllegalArgumentException("Either group name or student must be specified");
        }

        lessonRepository.save(lesson);

        List<LessonParticipant> participants = students.stream().map(student -> {
            LessonParticipant p = new LessonParticipant();
            p.setLesson(lesson);
            p.setStudent(student);
            p.setAttendanceStatus(LessonAttendanceStatus.PLANNED);
            return p;
        }).toList();
        lessonParticipantRepository.saveAll(participants);

        Integer repeatWeeks = request.getRepeatWeeks();
        if (repeatWeeks != null && repeatWeeks > 0) {
            for (int i = 1; i <= repeatWeeks; i++) {
                ZonedDateTime repeatedDate = utcDate.plusWeeks(i);
                Lesson repeatedLesson = new Lesson();
                repeatedLesson.setTeacher(teacher);
                repeatedLesson.setDateUtc(repeatedDate);
                repeatedLesson.setStatus(LessonStatus.PLANNED);
                if (lesson.getGroup() != null) {
                    repeatedLesson.setGroup(lesson.getGroup());
                }

                lessonRepository.save(repeatedLesson);

                List<LessonParticipant> repeatedParticipants = students.stream().map(student -> {
                    LessonParticipant p = new LessonParticipant();
                    p.setLesson(repeatedLesson);
                    p.setStudent(student);
                    p.setAttendanceStatus(LessonAttendanceStatus.PLANNED);
                    return p;
                }).toList();
                lessonParticipantRepository.saveAll(repeatedParticipants);
            }
        }

        List<Long> studentIds = students.stream().map(User::getId).toList();
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

        List<LessonParticipant> participants = lessonParticipantRepository.findAllByLessonId(lesson.getId());
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
        Long teacherId = authHelper.getCurrentUser().getId();

        List<Lesson> lessons = getLessonsBetween(start, end, teacherId);

        Map<Long, List<LessonParticipant>> participantsMap = lessonParticipantRepository
                .findAllByLessonIn(lessons).stream()
                .collect(Collectors.groupingBy(p -> p.getLesson().getId()));

        return lessons.stream()
                .map(lesson -> {
                    List<String> studentNames = participantsMap.getOrDefault(lesson.getId(), List.of()).stream()
                            .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                            .toList();

                    return new LessonShortCardResponse(
                            lesson.getId(),
                            lesson.getDateUtc(),
                            lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                            studentNames,
                            lesson.getStatus()
                    );
                })
                .toList();
    }

    public Map<String, List<LessonShortCardResponse>> getLessonsByWeek(LocalDate startDate, String timeZone, String email) {
        ZoneId zone = ZoneId.of(timeZone);

        ZonedDateTime startOfWeekLocal = startDate.atStartOfDay(zone);
        ZonedDateTime endOfWeekLocal = startOfWeekLocal.plusDays(7);

        ZonedDateTime startUtc = startOfWeekLocal.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUtc = endOfWeekLocal.withZoneSameInstant(ZoneId.of("UTC"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<Lesson> lessons;

        if (user.getRole() == Role.TEACHER) {
            lessons = lessonRepository.findAllByTeacherAndDateUtcBetween(user, startUtc, endUtc);
        } else if (user.getRole() == Role.STUDENT) {
            List<LessonParticipant> participants = lessonParticipantRepository.findAllByStudentId(user.getId());
            lessons = participants.stream()
                    .map(LessonParticipant::getLesson)
                    .filter(l -> !l.getDateUtc().isBefore(startUtc) && !l.getDateUtc().isAfter(endUtc))
                    .toList();
        } else {
            throw new IllegalStateException("Unsupported role: " + user.getRole());
        }

        Map<Long, List<LessonParticipant>> participantsMap = lessonParticipantRepository
                .findAllByLessonIn(lessons).stream()
                .collect(Collectors.groupingBy(p -> p.getLesson().getId()));

        List<LessonShortCardResponse> responseList = lessons.stream()
                .map(lesson -> {
                    List<String> studentNames = participantsMap.getOrDefault(lesson.getId(), List.of()).stream()
                            .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                            .toList();

                    return new LessonShortCardResponse(
                            lesson.getId(),
                            lesson.getDateUtc(),
                            lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                            studentNames,
                            lesson.getStatus()
                    );
                })
                .toList();


        return responseList.stream()
                .collect(Collectors.groupingBy(
                        l -> l.dateUtc().withZoneSameInstant(zone).getDayOfWeek().name(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
    @Transactional
    public void deleteLessonById(Long lessonId) {
        lessonParticipantRepository.deleteAllByLessonId(lessonId);
        lessonRepository.deleteById(lessonId);
    }
}