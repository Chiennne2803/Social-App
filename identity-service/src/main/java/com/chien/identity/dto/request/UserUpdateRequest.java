package com.chien.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.chien.identity.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    List<String> roles;
}
