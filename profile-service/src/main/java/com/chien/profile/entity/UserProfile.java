package com.chien.profile.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    String id;
    @Indexed(unique = true)
    String userId;
    String gender;
    String description;
    Date dob;
    String imgAvatar;
    String imgCover;
    List<Visitor> visitors;
    List<Friend> friends;

    Date createdAt;
    Date updatedAt;

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
