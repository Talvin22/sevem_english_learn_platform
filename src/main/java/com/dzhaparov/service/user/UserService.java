package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<User, UserDtoRequest> {
    User create(UserDtoRequest request);

    List<User> readAll();

    User getById(Long id);

    User updateById(Long id, UserDtoRequest request);

    boolean deleteById(Long id);

}