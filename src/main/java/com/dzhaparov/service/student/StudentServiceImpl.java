package com.dzhaparov.service.student;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.dto.homework.request.HomeworkDtoSubmitRequest;
import com.dzhaparov.dto.homework.response.HomeworkDtoListResponse;
import com.dzhaparov.dto.homework.response.HomeworkDtoSubmitResponse;
import com.dzhaparov.dto.lesson.response.LessonDtoListResponse;
import com.dzhaparov.dto.user.response.UserProfileDtoResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public LessonDtoListResponse getMyLessons(Long studentId) {
        return new LessonDtoListResponse(Collections.emptyList());
    }

    @Override
    public HomeworkDtoListResponse getMyHomeworks(Long studentId) {
        return new HomeworkDtoListResponse(
                null, // id
                null, // lessonId
                null, // lessonDate
                null, // status
                null  // grade
        );
    }

    @Override
    public HomeworkDtoSubmitResponse submitHomework(Long studentId, HomeworkDtoSubmitRequest request) {
        return new HomeworkDtoSubmitResponse(0,null,false, null, null, null,null,null, null);
    }

    @Override
    public GroupDtoResponse getMyGroup(Long studentId) {
        return new GroupDtoResponse(
                0, null, false, null, null, null
        );
    }

    @Override
    public UserProfileDtoResponse getProfile(Long studentId) {
        return new UserProfileDtoResponse(
                0L, null, null, null, null, null, null
        );
    }
}