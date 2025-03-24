package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.dto.user.response.UserDtoByIdResponse;
import com.dzhaparov.dto.user.response.UserDtoCreateResponse;
import com.dzhaparov.dto.user.response.UserDtoListResponse;
import com.dzhaparov.dto.user.response.UserDtoUpdateResponse;

import java.util.List;

public interface UserService {
    UserDtoCreateResponse createUser(UserDtoRequest userDto);

    UserDtoUpdateResponse updateUser(Long id, UserDtoRequest userDto);

    void deleteUser(Long id);

    UserDtoByIdResponse getUser(Long id);

    List<UserDtoListResponse> getAllUsers();
}