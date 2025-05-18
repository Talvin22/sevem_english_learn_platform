package com.dzhaparov.service.homework;


import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.request.HomeworkGroupSummaryDto;
import com.dzhaparov.dto.homework.response.*;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.entity.role.Role;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LessonParticipantRepository lessonParticipantRepository;

    public HomeworkServiceImpl(HomeworkRepository homeworkRepository,
                               LessonRepository lessonRepository,
                               UserRepository userRepository,
                               GroupRepository groupRepository,
                               LessonParticipantRepository lessonParticipantRepository) {
        this.homeworkRepository = homeworkRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.lessonParticipantRepository = lessonParticipantRepository;
    }

    @Override
    public List<HomeworkDtoResponse> createHomework(CreateHomeworkRequest request, Long teacherId) {
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        List<Homework> createdHomeworks = new ArrayList<>();

        if (request.groupId() != null) {
            Group group = groupRepository.findById(request.groupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group not found"));
            for (User student : group.getStudents()) {
                Homework hw = new Homework();
                hw.setLesson(lesson);
                hw.setStudent(student);
                hw.setGroup(group);
                hw.setContent(request.content());
                hw.setStatus(HomeworkStatus.NOT_SUBMITTED);
                createdHomeworks.add(homeworkRepository.save(hw));
            }
        } else if (request.studentId() != null) {
            User student = userRepository.findById(request.studentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            Homework hw = new Homework();
            hw.setLesson(lesson);
            hw.setStudent(student);
            hw.setContent(request.content());
            hw.setStatus(HomeworkStatus.NOT_SUBMITTED);
            createdHomeworks.add(homeworkRepository.save(hw));
        } else if (lesson.getGroup() != null) {
            Group group = lesson.getGroup();
            for (User student : group.getStudents()) {
                Homework hw = new Homework();
                hw.setLesson(lesson);
                hw.setStudent(student);
                hw.setGroup(group);
                hw.setContent(request.content());
                hw.setStatus(HomeworkStatus.NOT_SUBMITTED);
                createdHomeworks.add(homeworkRepository.save(hw));
            }
        } else {
            List<LessonParticipant> participants = lessonParticipantRepository.findAllByLessonId(lesson.getId());
            if (participants.size() == 1) {
                User student = participants.get(0).getStudent();
                Homework hw = new Homework();
                hw.setLesson(lesson);
                hw.setStudent(student);
                hw.setContent(request.content());
                hw.setStatus(HomeworkStatus.NOT_SUBMITTED);
                createdHomeworks.add(homeworkRepository.save(hw));
            } else {
                throw new IllegalArgumentException("Cannot determine target for homework: no group and ambiguous participants.");
            }
        }

        return createdHomeworks.stream()
                .map(HomeworkDtoResponse::from)
                .toList();
    }

    @Override
    public List<HomeworkDtoResponse> getHomeworksForStudent(Long studentId) {
        return homeworkRepository.findByStudentId(studentId).stream()
                .map(HomeworkDtoResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<HomeworkDtoResponse> getHomeworksToCheckForTeacher(Long teacherId) {
        return homeworkRepository.findByLessonTeacherId(teacherId).stream()
                .filter(hw -> hw.getStatus() != HomeworkStatus.NOT_SUBMITTED)
                .map(HomeworkDtoResponse::from)
                .collect(Collectors.toList());
    }
    public HomeworkDtoDetailResponse getHomeworkById(Long id, User user) {
        Homework hw = homeworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (user.getRole() == Role.STUDENT && !hw.getStudent().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: Not your homework");
        }

        if (user.getRole() == Role.TEACHER && !hw.getLesson().getTeacher().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: You are not the teacher");
        }

        return HomeworkDtoDetailResponse.from(hw);
    }

    @Override
    public HomeworkGroupSummaryListResponse getGroupedHomeworksToCheck(Long teacherId) {
        List<Homework> homeworks = homeworkRepository.findByLessonTeacherId(teacherId);

        Map<String, List<Homework>> grouped = homeworks.stream()
                .collect(Collectors.groupingBy(hw -> {
                    Long lessonId = hw.getLesson().getId();
                    String groupKey = hw.getGroup() != null ? hw.getGroup().getName() : "–";
                    return lessonId + "|" + groupKey;
                }));

        List<HomeworkGroupSummaryDto> summaryList = grouped.entrySet().stream()
                .map(entry -> {
                    List<Homework> hwList = entry.getValue();
                    Homework sample = hwList.get(0);

                    String groupName = sample.getGroup() != null ? sample.getGroup().getName() : null;

                    String studentName = (groupName == null && sample.getStudent() != null)
                            ? sample.getStudent().getFirst_name() + " " + sample.getStudent().getLast_name()
                            : null;

                    int total = hwList.size();
                    int checked = (int) hwList.stream()
                            .filter(hw -> hw.getStatus() != HomeworkStatus.NOT_SUBMITTED)
                            .count();

                    return new HomeworkGroupSummaryDto(
                            sample.getLesson().getId(),
                            studentName,
                            sample.getLesson().getDateUtc().toLocalDateTime(),
                            groupName,
                            total,
                            checked
                    );
                })
                .toList();

        return HomeworkGroupSummaryListResponse.of(summaryList);
    }

    @Override
    public HomeworkDtoListResponse getHomeworksByLessonId(Long lessonId) {
        List<Homework> homeworks = homeworkRepository.findByLessonId(lessonId);
        List<HomeworkDtoDetailResponse> dtos = homeworks.stream()
                .map(HomeworkDtoDetailResponse::from)
                .toList();
        return HomeworkDtoListResponse.of(dtos);
    }

    @Override
    public HomeworkDtoResponse updateHomework(HomeworkDtoGradeRequest request) {
        Homework homework = homeworkRepository.findById(request.homeworkId())
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (request.grade() != null) {
            homework.setGrade(request.grade());
        }

        if (request.content() != null) {
            homework.setContent(request.content());
        }

        homeworkRepository.save(homework);

        return HomeworkDtoResponse.from(homework);
    }
    public void submitHomework(Long homeworkId, Long studentId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (!homework.getStudent().getId().equals(studentId)) {
            throw new AccessDeniedException("You cannot submit this homework");
        }

        homework.setStatus(HomeworkStatus.SUBMITTED);
        homeworkRepository.save(homework);
    }
}
