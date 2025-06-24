package com.chien.profile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String gender;
    String description;
    Date createdAt;
    Date updatedAt;

    List<Visitor> visitors;
    List<Friend> friends;

    @Data
    public static class Visitor {
        String userId;
        Date visitedAt;
    }

    @Data
    public static class Friend {
        String userId;
    }
}
