package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.request.CreateGroupRequest;
import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.*;
import com.dzhaparov.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.service.homework.HomeworkService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final GroupRepository groupRepository;
    private final LessonParticipantRepository lessonParticipantRepository;
    private final AuthHelper authHelper;
    private final HomeworkService homeworkService;

    public TeacherServiceImpl(UserRepository userRepository,
                              LessonRepository lessonRepository,
                              HomeworkRepository homeworkRepository,
                              GroupRepository groupRepository,
                              LessonParticipantRepository lessonParticipantRepository,
                              AuthHelper authHelper,
                              HomeworkService homeworkService
    ) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.groupRepository = groupRepository;
        this.lessonParticipantRepository = lessonParticipantRepository;
        this.authHelper = authHelper;
        this.homeworkService = homeworkService;
    }

    @Override
    public List<UserProfileDtoResponse> getMyStudents(Long teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId).stream()
                .map(user -> new UserProfileDtoResponse(
                        user.getId(),
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getRole(),
                        user.getGroups().stream()
                                .map(g -> new GroupShortDto(g.getId(), g.getName()))
                                .toList(),
                        user.getSalaryPerLesson()
                ))
                .toList();
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long teacherId) {
        List<Lesson> lessons = lessonRepository.findByTeacherId(teacherId);
        List<LessonParticipant> allParticipants = lessonParticipantRepository.findAllByLessonIn(lessons);

        Map<Long, List<LessonParticipant>> participantsByLessonId = allParticipants.stream()
                .collect(Collectors.groupingBy(p -> p.getLesson().getId()));

        List<LessonDtoDetailResponse> dtoList = lessons.stream().map(lesson -> {
            List<LessonParticipant> participants = participantsByLessonId.getOrDefault(lesson.getId(), List.of());

            List<String> studentNames = participants.stream()
                    .map(p -> p.getStudent().getFirst_name() + " " + p.getStudent().getLast_name())
                    .collect(Collectors.toList());

            LessonParticipant firstParticipant = participants.isEmpty() ? null : participants.get(0);

            return new LessonDtoDetailResponse(
                    lesson.getId(),
                    lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                    studentNames,
                    lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                    lesson.getDateUtc(),
                    lesson.getStatus(),
                    lesson.getCancelingReason(),
                    firstParticipant != null ? firstParticipant.getAttendanceStatus() : null,
                    lesson.getCancelledBy()
            );
        }).toList();

        return LessonDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoListResponse getHomeworksToCheck(Long teacherId) {
        var homeworks = homeworkRepository.findByLessonTeacherId(teacherId);
        var dtoList = homeworks.stream()
                .map(HomeworkDtoDetailResponse::from)
                .toList();
        return HomeworkDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoGradeResponse gradeHomework(HomeworkDtoGradeRequest request) {
        var homework = homeworkRepository.findById(request.homeworkId()).orElseThrow();
        homework.setGrade(request.grade());
        homeworkRepository.save(homework);

        return new HomeworkDtoGradeResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                true,
                "Homework graded successfully",
                homework.getId(),
                homework.getGrade(),
                homework.getStatus().name()
        );
    }

    @Override
    @Transactional
    public GroupDtoResponse addStudentToGroup(Long groupId, Long studentId) {
        var student = userRepository.findById(studentId).orElseThrow();
        var group = groupRepository.findById(groupId).orElseThrow();


        group.getStudents().add(student);
        student.getGroups().add(group);

        groupRepository.save(group);
        return GroupDtoResponse.of(true, group);
    }

    @Override
    public UserProfileDtoResponse getProfile(Long teacherId) {
        var user = userRepository.findById(teacherId).orElseThrow();
        return new UserProfileDtoResponse(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getRole(),
                null,
                user.getSalaryPerLesson()
        );
    }

    @Override
    public List<GroupShortDto> getGroupsForTeacher(Long teacherId) {
        return groupRepository.findByTeacherId(teacherId)
                .stream()
                .map(g -> new GroupShortDto(g.getId(), g.getName()))
                .toList();
    }
    @Override
    public void removeStudentFromGroup(Long groupId, Long studentId) {
        var group = groupRepository.findById(groupId).orElseThrow();
        var student = userRepository.findById(studentId).orElseThrow();

        group.getStudents().remove(student);
        student.getGroups().remove(group);

        groupRepository.save(group);
        userRepository.save(student);
    }

    @Override
    public List<UserProfileDtoResponse> getStudentsWithoutGroups() {
        return userRepository.findAll().stream()
                .filter(user -> user.isStudent() && (user.getGroups() == null || user.getGroups().isEmpty()))
                .map(user -> new UserProfileDtoResponse(
                        user.getId(),
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getRole(),
                        Collections.emptyList(),
                        user.getSalaryPerLesson()
                ))
                .toList();
    }
    @Override
    public GroupDtoResponse createGroup(CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.name());
        group.setTeacher(userRepository.findById(authHelper.getCurrentUser().getId()).orElseThrow());
        group.setActive(true);
        return GroupDtoResponse.of(true, groupRepository.save(group));
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
    @Override
    public void assignStudentToTeacher(Long teacherId, Long studentId) {
        var teacher = userRepository.findById(teacherId).orElseThrow();
        var student = userRepository.findById(studentId).orElseThrow();

        if (!student.isStudent()) {
            throw new IllegalArgumentException("User is not a student.");
        }

        student.setTeacher(teacher);
        userRepository.save(student);
    }

    @Override
    public void unassignStudentFromTeacher(Long studentId) {
        var student = userRepository.findById(studentId).orElseThrow();
        student.setTeacher(null);
        userRepository.save(student);
    }
    public HomeworkGroupSummaryListResponse getGroupedHomeworksToCheck(Long teacherId) {
        return homeworkService.getGroupedHomeworksToCheck(teacherId);
    }
    @Override
    public HomeworkDtoGradeResponse updateHomeworkAsTeacher(HomeworkDtoGradeRequest request, Long teacherId) {
        Homework homework = homeworkRepository.findById(request.homeworkId())
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (!homework.getLesson().getTeacher().getId().equals(teacherId)) {
            throw new AccessDeniedException("You cannot modify this homework");
        }

        HomeworkDtoResponse updated = homeworkService.updateHomework(request);

        return new HomeworkDtoGradeResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                true,
                "Homework updated successfully",
                updated.id(),
                updated.grade(),
                updated.status().name()
        );
    }
}
