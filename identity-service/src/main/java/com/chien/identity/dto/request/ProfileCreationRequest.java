package com.chien.identity.dto.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    private String gender;
    private String description;
    private Date dob;
    private String imgAvatar;
    private String imgCover;
}
