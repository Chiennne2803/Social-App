package com.chien.identity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chien.identity.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {}
