package com.chien.identity.dto.request;

import java.util.Set;

import com.chien.identity.dto.response.PermissionResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
