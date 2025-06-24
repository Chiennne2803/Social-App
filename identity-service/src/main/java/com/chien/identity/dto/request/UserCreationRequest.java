package com.chien.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Email(message = "INVALID_EMAIL")
    @Size(min = 5, message = "EMAIL_TOO_SHORT")
    private String email;

    @Size(min = 5, message = "USERNAME_TOO_SHORT")
    private String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;
}
