package com.dzhaparov.controller.student;

import com.dzhaparov.dto.group.response.GroupDtoResponse;
import com.dzhaparov.util.AuthHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final AuthHelper authHelper;

    public StudentController(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    @GetMapping("/groups")
    public List<GroupDtoResponse> getMyGroups() {
        var user = authHelper.getCurrentUser();
        return user.getGroups().stream()
                .map(GroupDtoResponse::ofSuccess)
                .toList();
    }
}