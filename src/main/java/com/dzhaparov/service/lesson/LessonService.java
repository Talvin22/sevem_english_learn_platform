package com.dzhaparov.service.lesson;

import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
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
import java.util.List;
import java.util.Optional;

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
    public void createLesson(CreateLessonRequest request, String email) {
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
        } else {
            throw new IllegalArgumentException("Either group name or student must be specified");
        }
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

        lesson.setAttendanceStatus(request.attendanceStatus());

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

        lessonRepository.save(lesson);
    }

    public LessonEditDtoResponse getLessonForEdit(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<String> studentNames = lessonParticipantRepository.findByLessonId(lessonId).stream()
                .map(lp -> lp.getStudent().getFirst_name() + " " + lp.getStudent().getLast_name())
                .toList();

        return new LessonEditDtoResponse(
                lesson.getId(),
                lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                String.join(", ", studentNames),
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                lesson.getDateUtc(),
                lesson.getStatus(),
                null,

                lesson.getCancelingReason(),
                lesson.getCancelledBy()
        );
    }
}