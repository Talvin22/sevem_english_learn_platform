package com.dzhaparov.infrastructure.web.mapper;

import com.dzhaparov.entity.group.Group;
import com.dzhaparov.infrastructure.web.dto.group.request.GroupShortDto;
import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoListResponse;
import com.dzhaparov.infrastructure.web.dto.group.response.GroupDtoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupMapper {

    public GroupDtoResponse toResponse(Group group, String message) {
        var teacher = group.getTeacher();
        return new GroupDtoResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                true,
                message,
                group.getId(),
                group.getName(),
                group.getActive(),
                teacher != null ? teacher.getFirst_name() + " " + teacher.getLast_name() : null,
                group.getStudents() != null ? group.getStudents().stream()
                        .map(s -> s.getFirst_name() + " " + s.getLast_name())
                        .collect(Collectors.toList()) : null,
                group.getLessons() != null ? group.getLessons().stream().map(l -> l.getId().toString()).toList() : null
        );
    }

    public GroupDtoListResponse toListResponse(List<Group> groups) {
        List<GroupDtoResponse> responses = groups.stream()
                .map(group -> toResponse(group, "Group fetched successfully."))
                .toList();
        return GroupDtoListResponse.of(responses);
    }

    public GroupShortDto toShort(Group group) {
        return new GroupShortDto(group.getId(), group.getName());
    }
}
