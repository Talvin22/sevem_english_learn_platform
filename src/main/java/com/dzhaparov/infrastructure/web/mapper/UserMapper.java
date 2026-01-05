package com.dzhaparov.infrastructure.web.mapper;

import com.dzhaparov.entity.user.User;
import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.infrastructure.web.dto.user.response.UserDtoDetailResponse;
import com.dzhaparov.infrastructure.web.dto.user.response.UserProfileDtoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDtoDetailResponse toDetail(User user) {
        return new UserDtoDetailResponse(user.getId(), user.getFirst_name(), user.getLast_name(), user.getEmail());
    }

    public UserProfileDtoResponse toProfile(User user) {
        List<GroupShortDto> groups = user.getGroups() != null ?
                user.getGroups().stream()
                        .map(group -> new GroupShortDto(group.getId(), group.getName()))
                        .toList() : List.of();

        return new UserProfileDtoResponse(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getRole(),
                groups,
                user.getSalaryPerLesson()
        );
    }
}
