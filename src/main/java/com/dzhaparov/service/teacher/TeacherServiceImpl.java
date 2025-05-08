package com.dzhaparov.service.teacher;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoGradeRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoGradeResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public TeacherServiceImpl(UserRepository userRepository,
                              LessonRepository lessonRepository,
                              HomeworkRepository homeworkRepository,
                              GroupRepository groupRepository,
                              LessonParticipantRepository lessonParticipantRepository
    ) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.groupRepository = groupRepository;
        this.lessonParticipantRepository = lessonParticipantRepository;
    }

    @Override
    public List<UserProfileDtoResponse> getMyStudents(Long teacherId) {
        var students = userRepository.findAllByGroup_Teacher_Id(teacherId);
        return students.stream()
                .map(user -> new UserProfileDtoResponse(
                        user.getId(),
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getRole(),
                        user.getGroup(),
                        user.getSalaryPerLesson()
                )).collect(Collectors.toList());
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long teacherId) {
        List<Lesson> lessons = lessonRepository.findByTeacherId(teacherId);
        List<LessonParticipant> participants = lessonParticipantRepository.findAllByLessonIn(lessons);

        Map<Lesson, List<LessonParticipant>> grouped = participants.stream()
                .collect(Collectors.groupingBy(LessonParticipant::getLesson));

        List<LessonDtoDetailResponse> dtoList = grouped.entrySet().stream()
                .map(entry -> {
                    Lesson lesson = entry.getKey();
                    List<LessonParticipant> lessonParticipants = entry.getValue();

                    String studentSummary = (lessonParticipants.size() == 1)
                            ? lessonParticipants.get(0).getStudent().getFirst_name() + " " + lessonParticipants.get(0).getStudent().getLast_name()
                            : "Group of " + lessonParticipants.size() + " students";

                    return new LessonDtoDetailResponse(
                            lesson.getId(),
                            lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                            studentSummary,
                            lesson.getGroup() != null ? lesson.getGroup().getName() : null,
                            lesson.getDateUtc(),
                            lesson.getStatus(),
                            lesson.getCancelingReason(),
                            null,
                            lesson.getCancelledBy()
                    );
                })
                .collect(Collectors.toList());

        return LessonDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoListResponse getHomeworksToCheck(Long teacherId) {
        var homeworks = homeworkRepository.findByLesson_Teacher_Id(teacherId);
        var dtoList = homeworks.stream()
                .map(hw -> new HomeworkDtoDetailResponse(
                        hw.getId(),
                        hw.getLesson().getId(),
                        hw.getLesson().getDateUtc().toLocalDateTime(),
                        hw.getStatus(),
                        hw.getGrade()
                )).collect(Collectors.toList());

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
    public GroupDtoResponse addStudentToGroup(Long groupId, Long studentId) {
        var student = userRepository.findById(studentId).orElseThrow();
        var group = groupRepository.findById(groupId).orElseThrow();

        student.setGroup(group);
        userRepository.save(student);

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
    public List<GroupDtoResponse> getGroupsForTeacher(Long teacherId) {
        var groups = groupRepository.findByTeacherId(teacherId);
        return groups.stream()
                .map(GroupDtoResponse::ofSuccess)
                .collect(Collectors.toList());
    }
}
