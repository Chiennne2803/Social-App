package com.chien.profile.dto.request;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    private String userId;
    private String username;
    private String gender;
    private String description;
    private Date dob;
    private String imgAvatar;
    private String imgCover;
}
