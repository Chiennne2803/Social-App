package com.chien.identity.dto.request;

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

    @NotBlank(message = "Gender is required")
    String gender;

    @Size(max = 255, message = "Description must be at most 255 characters")
    String description;

    @Past(message = "Date of birth must be in the past")
    Date dob;

    @URL(message = "Avatar image must be a valid URL")
    String imgAvatar;

    @URL(message = "Cover image must be a valid URL")
    String imgCover;
}
