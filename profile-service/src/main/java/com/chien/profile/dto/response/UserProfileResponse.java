package com.chien.profile.dto.response;

import java.util.Date;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String userId;
    String gender;
    String description;
    String username;
    Date createdAt;
    Date updatedAt;
    Date dob;
    String imgAvatar;
    String imgCover;
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
