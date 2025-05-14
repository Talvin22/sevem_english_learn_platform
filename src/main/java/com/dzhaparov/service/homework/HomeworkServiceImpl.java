package com.dzhaparov.service.homework;


import com.dzhaparov.dto.homework.request.CreateHomeworkRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoResponse;
import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.homework.Homework;
import com.dzhaparov.entity.homework.HomeworkStatus;
import com.dzhaparov.entity.lesson.Lesson;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.group.GroupRepository;
import com.dzhaparov.repository.homework.HomeworkRepository;
import com.dzhaparov.repository.lesson.LessonRepository;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public HomeworkServiceImpl(HomeworkRepository homeworkRepository,
                               LessonRepository lessonRepository,
                               UserRepository userRepository,
                               GroupRepository groupRepository) {
        this.homeworkRepository = homeworkRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void createHomework(CreateHomeworkRequest request) {
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (request.groupId() != null) {
            Group group = groupRepository.findById(request.groupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group not found"));
            for (User student : group.getStudents()) {
                Homework homework = new Homework();
                homework.setLesson(lesson);
                homework.setStudent(student);
                homework.setGroup(group);
                homework.setContent(request.content());
                homework.setStatus(HomeworkStatus.NOT_SUBMITTED);
                homeworkRepository.save(homework);
            }
        } else if (request.studentId() != null) {
            User student = userRepository.findById(request.studentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            Homework homework = new Homework();
            homework.setLesson(lesson);
            homework.setStudent(student);
            homework.setContent(request.content());
            homework.setStatus(HomeworkStatus.NOT_SUBMITTED);
            homeworkRepository.save(homework);
        } else {
            throw new IllegalArgumentException("Either groupId or studentId must be provided");
        }
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
                .filter(hw -> hw. != HomeworkStatus.NOT_SUBMITTED)
                .map(HomeworkDtoResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
