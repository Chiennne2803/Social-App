package com.chien.identity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.chien.identity.entity.Permission;
import com.chien.identity.repository.PermissionRepository;

@Component
public class StringToPermissionConverter implements Converter<String, Permission> {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        return permissionRepository
                .findById(source)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found for id: " + source));
    }
}
