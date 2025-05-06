package com.dzhaparov.service.lesson;

import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
import com.dzhaparov.dto.lesson.request.LessonRequestDto;
import com.dzhaparov.dto.lesson.request.UpdateLessonStatusRequest;
import com.dzhaparov.dto.lesson.response.LessonEditDtoResponse;
import com.dzhaparov.dto.user.response.UserDtoDetailResponse;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonStatus;
import com.dzhaparov.entity.lesson.attendance.LessonAttendanceStatus;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.util.AuthHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final AuthHelper authHelper;

    public LessonService(LessonRepository lessonRepository, UserRepository userRepository, GroupRepository groupRepository, AuthHelper authHelper) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.authHelper = authHelper;

    }

    public void createLesson(CreateLessonRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Lesson> lessonsToSave = new ArrayList<>();

        boolean hasGroup = request.getGroupName() != null && !request.getGroupName().isBlank();
        boolean hasStudent = request.getStudentId() != null;

        if (hasGroup && hasStudent) {
            throw new IllegalArgumentException("Specify either group or student, not both.");
        }

        if (hasGroup) {
            Group group = groupRepository.findByName(request.getGroupName())
                    .orElseThrow(() -> new RuntimeException("Group not found: " + request.getGroupName()));

            for (User student : group.getStudents()) {
                Lesson lesson = buildLesson(request, teacher, student, group);
                lessonsToSave.add(lesson);
            }

        } else if (hasStudent) {
            User student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + request.getStudentId()));

            Lesson lesson = buildLesson(request, teacher, student, null);
            lessonsToSave.add(lesson);

        } else {
            throw new IllegalArgumentException("Either student or group must be provided.");
        }

        lessonRepository.saveAll(lessonsToSave);
    }

    private Lesson buildLesson(CreateLessonRequest request, User teacher, User student, Group group) {
        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setGroup(group);
        lesson.setDateUtc(request.getDateUtc().atZone(ZoneId.of("UTC")));
        lesson.setStatus(LessonStatus.PLANNED);
        lesson.setAttendanceStatus(LessonAttendanceStatus.PLANNED);
        return lesson;
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

        return new LessonEditDtoResponse(
                lesson.getId(),
                lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                lesson.getStudent().getFirst_name() + " " + lesson.getStudent().getLast_name(),
                lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                lesson.getDateUtc(),
                lesson.getStatus(),
                lesson.getAttendanceStatus(),
                lesson.getCancelingReason(),
                lesson.getCancelledBy()
        );
    }
}