package com.dzhaparov.service.student;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoDetailResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoDetailResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(LessonRepository lessonRepository, HomeworkRepository homeworkRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LessonDtoListResponse getMyLessons(Long studentId) {
        var lessons = lessonRepository.findByStudentId(studentId);
        var dtoList = lessons.stream()
                .map(lesson -> new LessonDtoDetailResponse(
                        lesson.getId(),
                        lesson.getTeacher().getFirst_name() + " " + lesson.getTeacher().getLast_name(),
                        lesson.getStudent().getFirst_name() + " " + lesson.getStudent().getLast_name(),
                        lesson.getGroup().getName(),
                        lesson.getDateUtc(),
                        lesson.getStatus(),
                        lesson.getCancelingReason(),
                        lesson.getCancelledBy()
                )).collect(Collectors.toList());

        return new LessonDtoListResponse(dtoList);
    }

    @Override
    public HomeworkDtoListResponse getMyHomeworks(Long studentId) {
        var homeworks = homeworkRepository.findByStudentId(studentId);
        var dtoList = homeworks.stream()
                .map(hw -> new HomeworkDtoDetailResponse(
                        hw.getId(),
                        hw.getLesson().getId(),
                        hw.getLesson().getDateUtc().toLocalDateTime(),
                        hw.getStatus(),
                        hw.getGrade()
                )).collect(Collectors.toList());

        return new HomeworkDtoListResponse(dtoList);
    }

    @Override
    public HomeworkDtoSubmitResponse submitHomework(Long studentId, HomeworkDtoSubmitRequest request) {
        return HomeworkDtoSubmitResponse.of(false, null);
    }

    @Override
    public GroupDtoResponse getMyGroup(Long studentId) {
        var user = userRepository.findById(studentId).orElseThrow();
        var group = user.getGroup();

        return new GroupDtoResponse(
                group.getId(),
                group.getName(),
                group.isActive(),
                group.getTeacher().getFirst_name() + " " + group.getTeacher().getLast_name(),
                null, // groupSchedule
                null // students
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
                user.getGroup() != null ? user.getGroup() : null,
                user.getSalaryPerLesson()
        );
    }
}
