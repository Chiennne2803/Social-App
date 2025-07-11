package com.chien.identity.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chien.identity.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
