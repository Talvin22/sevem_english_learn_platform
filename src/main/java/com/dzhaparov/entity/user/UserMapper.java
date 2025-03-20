package com.dzhaparov.entity.user;


import com.dzhaparov.dto.user.request.UserDtoRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserDtoRequest dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.id());
        user.setRoles(dto.roles());
        user.setFirst_name(dto.first_name());
        user.setLast_name(dto.last_name());
        user.setEmail(dto.email());
        user.setHashed_password(dto.password());
        user.setSalaryPerLesson(Double.parseDouble(dto.salaryPerLesson()));
        return user;
    }

    public UserDtoRequest toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDtoRequest(
                user.getId(),
                user.getRoles(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getHashed_password(),
                user.getSalaryPerLesson().toString()
        );
    }
}