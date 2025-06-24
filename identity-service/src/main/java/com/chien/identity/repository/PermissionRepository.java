package com.chien.identity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chien.identity.entity.Permission;

public interface PermissionRepository extends MongoRepository<Permission, String> {}
