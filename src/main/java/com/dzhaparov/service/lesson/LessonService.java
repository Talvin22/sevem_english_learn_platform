package com.dzhaparov.service.lesson;

import com.dzhaparov.dto.lesson.request.CreateLessonRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public LessonService(LessonRepository lessonRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public void createLesson(CreateLessonRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("Student must be selected");
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lesson.setDateUtc(request.getDateUtc().atZone(ZoneId.of("UTC")));
        lesson.setStatus(LessonStatus.PLANNED);
        lesson.setAttendanceStatus(LessonAttendanceStatus.PLANNED);


        if (request.getGroupName() != null && !request.getGroupName().isBlank()) {
            Group group = groupRepository.findByName(request.getGroupName())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            lesson.setGroup(group);
        }

        lessonRepository.save(lesson);
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