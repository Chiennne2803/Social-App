package com.chien.identity.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;
    private String password;
    private List<String> roles;
    private LocalDate dob;

    private Date createdAt;
    private Date updatedAt;
}
