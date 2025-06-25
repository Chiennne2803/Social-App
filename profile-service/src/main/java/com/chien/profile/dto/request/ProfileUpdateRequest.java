package com.chien.profile.dto.request;

import static lombok.AccessLevel.PRIVATE;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ProfileUpdateRequest {
    private String gender;
    private String description;
    private Date dob;
    private String imgAvatar;
    private String imgCover;

    private List<Visitor> visitors;
    private List<Friend> friends;

    @Data
    public static class Visitor {
        private String userId;
        private Date visitedAt;
    }

    @Data
    public static class Friend {
        private String userId;
    }
}
