package com.dzhaparov.dto.user;

import com.dzhaparov.entity.roles.Roles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoRequest(Long id,
                             Roles roles,
                             String first_name,
                             String last_name,
                             String email,
                             String password,
                             String salaryPerLesson) {
}
