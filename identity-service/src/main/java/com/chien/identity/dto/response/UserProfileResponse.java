package com.chien.identity.dto.response;

import java.time.LocalDate;
import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
    private Date createdAt;
    private Date updatedAt;
}
