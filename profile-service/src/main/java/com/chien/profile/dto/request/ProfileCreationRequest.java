package com.chien.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)?$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;

    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;

    @Past(message = "Date of birth must be in the past")
    private Date dob;

    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            message = "Avatar URL must be a valid URL"
    )
    private String imgAvatar;

    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            message = "Cover image URL must be a valid URL"
    )
    private String imgCover;
}
