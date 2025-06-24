package com.chien.identity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chien.identity.entity.InvalidatedToken;

@Repository
public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> {}
