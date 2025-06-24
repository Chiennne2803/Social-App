package com.chien.profile.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
    String userId;
    String gender;
    String description;

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
