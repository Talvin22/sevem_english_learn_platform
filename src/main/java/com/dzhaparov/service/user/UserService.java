package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.dto.user.response.UserDtoCreateResponse;
import com.dzhaparov.dto.user.response.UserDtoUpdateResponse;

import java.util.List;

public interface UserService {
    UserDtoCreateResponse createUser(UserDtoRequest userDto);

    UserDtoUpdateResponse updateUser(Long id, UserDtoRequest userDto);//TODO

    void deleteUser(Long id);

    UserDtoResponse getUser(Long id);

    List<UserDtoResponse> getAllUsers();
}