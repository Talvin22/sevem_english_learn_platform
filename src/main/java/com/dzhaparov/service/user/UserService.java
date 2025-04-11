package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User, UserDtoRequest> {
    User create(UserDtoRequest request);

    Optional<List<User>> readAllStudentsOfTeacher();

    User getById(Long id);

    User updateById(Long id, UserDtoRequest request);

    boolean deleteById(Long id);

}