package com.dzhaparov.service.student;

public interface StudentService {
    List<LessonDto> getMyLessons(Long studentId);
    List<HomeworkDto> getMyHomeworks(Long studentId);
    HomeworkDto submitHomework(Long studentId, HomeworkSubmitRequest request);
    GroupDto getMyGroup(Long studentId);
}
