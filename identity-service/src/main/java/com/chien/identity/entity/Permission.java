package com.chien.identity.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document(collection = "permissions")
@Builder
public class Permission {
    @Id
    private String name;

    private String description;
}
