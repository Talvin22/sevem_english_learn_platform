package com.dzhaparov.service.student;

import com.dzhaparov.dto.group.request.GroupShortDto;
import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LessonParticipantRepository lessonParticipantRepository;

    public StudentServiceImpl(LessonRepository lessonRepository, HomeworkRepository homeworkRepository, GroupRepository groupRepository, UserRepository userRepository, LessonParticipantRepository lessonParticipantRepository) {
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.lessonParticipantRepository = lessonParticipantRepository;
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long studentId) {
        List<LessonParticipant> participants = lessonParticipantRepository.findAllByStudentId(studentId);

        List<LessonDtoDetailResponse> dtoList = participants.stream()
                .map(participant -> {
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
                })
                .collect(Collectors.toList());
        return LessonDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoListResponse getMyHomeworks(Long studentId) {
        var homeworks = homeworkRepository.findByStudentId(studentId);
        var dtoList = homeworks.stream()
                .map(HomeworkDtoDetailResponse::from)
                .toList();

        return HomeworkDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoSubmitResponse submitHomework(Long studentId, HomeworkDtoSubmitRequest request) {
        return HomeworkDtoSubmitResponse.of(false, null);
    }

    @Override
    public GroupDtoResponse getMyGroup(Long studentId) {
        var user = userRepository.findById(studentId).orElseThrow();
        var groups = user.getGroups();

        if (groups == null || groups.isEmpty()) {
            return new GroupDtoResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    true,
                    "User is not assigned to any group.",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        var group = groups.get(0);
        var teacher = group.getTeacher();

        return new GroupDtoResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                true,
                "Group fetched successfully.",
                group.getId(),
                group.getName(),
                group.getActive(),
                teacher != null ? teacher.getFirst_name() + " " + teacher.getLast_name() : null,
                null,
                null
        );
    }

    @Override
    public UserProfileDtoResponse getProfile(Long studentId) {
        var user = userRepository.findById(studentId).orElseThrow();

        return new UserProfileDtoResponse(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getRole(),
                user.getGroups().stream()
                        .map(g -> new GroupShortDto(g.getId(), g.getName()))
                        .toList(),
                user.getSalaryPerLesson()
        );
    }
}
