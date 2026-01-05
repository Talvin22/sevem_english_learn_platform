package com.dzhaparov.application.service.student;

import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoResponse;
import com.dzhaparov.infrastructure.web.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.infrastructure.web.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.lesson.LessonParticipant;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonParticipantRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import com.dzhaparov.infrastructure.web.mapper.GroupMapper;
import com.dzhaparov.infrastructure.web.mapper.HomeworkMapper;
import com.dzhaparov.infrastructure.web.mapper.LessonMapper;
import com.dzhaparov.infrastructure.web.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LessonParticipantRepository lessonParticipantRepository;
    private final LessonMapper lessonMapper;
    private final HomeworkMapper homeworkMapper;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    public StudentServiceImpl(LessonRepository lessonRepository,
                             HomeworkRepository homeworkRepository,
                             GroupRepository groupRepository,
                             UserRepository userRepository,
                             LessonParticipantRepository lessonParticipantRepository,
                             LessonMapper lessonMapper,
                             HomeworkMapper homeworkMapper,
                             GroupMapper groupMapper,
                             UserMapper userMapper) {
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.lessonParticipantRepository = lessonParticipantRepository;
        this.lessonMapper = lessonMapper;
        this.homeworkMapper = homeworkMapper;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long studentId) {
        List<LessonParticipant> participants = lessonParticipantRepository.findAllByStudentId(studentId);

        List<LessonDtoDetailResponse> dtoList = participants.stream()
                .map(lessonMapper::toDetailResponse)
                .collect(Collectors.toList());
        return LessonDtoListResponse.of(dtoList);
    }

    @Override
    public HomeworkDtoListResponse getMyHomeworks(Long studentId) {
        var homeworks = homeworkRepository.findByStudentId(studentId);
        var dtoList = homeworks.stream()
                .map(homeworkMapper::toDetail)
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

        return groupMapper.toResponse(group, "Group fetched successfully.");
    }

    @Override
    public UserProfileDtoResponse getProfile(Long studentId) {
        var user = userRepository.findById(studentId).orElseThrow();

        return userMapper.toProfile(user);
    }
}
