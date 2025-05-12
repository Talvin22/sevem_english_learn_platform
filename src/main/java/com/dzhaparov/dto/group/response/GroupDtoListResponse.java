package com.dzhaparov.dto.group.response;


import java.util.List;

public record GroupDtoListResponse(
        int statusCode,
        String reasonPhrase,
        boolean success,
        String message,
        List<GroupDtoResponse> groups
) {
    public static GroupDtoListResponse of(List<GroupDtoResponse> groupList) {
        return new GroupDtoListResponse(
                200,
                "OK",
                true,
                "Groups fetched successfully.",
                groupList
        );
    }

    public static GroupDtoListResponse empty() {
        return new GroupDtoListResponse(
                200,
                "OK",
                true,
                "No groups found.",
                List.of()
        );
    }
}