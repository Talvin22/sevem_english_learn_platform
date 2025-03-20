package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.service.BaseServiceMethod;

public interface UserService extends BaseServiceMethod<Long, UserDtoRequest> {
    void createUser(UserDtoRequest userDto);

    void deleteUser(UserDtoRequest userDto);

    void updateUser(Long id, UserDtoRequest userDto);

    UserDtoRequest getUser(Long id);
}
