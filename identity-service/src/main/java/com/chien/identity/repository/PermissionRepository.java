package com.chien.identity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chien.identity.entity.Permission;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    List<Permission> findAllById(Iterable<String> id);
}
