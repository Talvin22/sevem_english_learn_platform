package com.dzhaparov.dto.user.request;

import com.dzhaparov.entity.group.Group;
import com.dzhaparov.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoRequest(Long id,
                             Role roles,
                             String first_name,
                             String last_name,
                             String email,
                             String password,
                             Double salaryPerLesson,
                             Group group) {
}
