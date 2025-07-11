package com.chien.identity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    private String id;
    private String name;

    public PermissionResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
