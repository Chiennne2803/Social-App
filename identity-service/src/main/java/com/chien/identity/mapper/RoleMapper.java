package com.chien.identity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.chien.identity.dto.request.RoleRequest;
import com.chien.identity.dto.response.PermissionResponse;
import com.chien.identity.dto.response.RoleResponse;
import com.chien.identity.entity.Permission;
import com.chien.identity.entity.Role;
import com.chien.identity.repository.PermissionRepository;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    @Autowired
    private PermissionRepository permissionRepository;

    @Mapping(target = "permissions", expression = "java(mapPermissions(request.getPermissions()))")
    public abstract Role toRole(RoleRequest request);

    public abstract RoleResponse toRoleResponse(Role role);

    protected List<Permission> mapPermissions(Set<PermissionResponse> permissionResponses) {
        if (permissionResponses == null || permissionResponses.isEmpty()) {
            return null;
        }

        Set<String> ids = permissionResponses.stream()
                .map(PermissionResponse::getId) // đảm bảo class này có getId()
                .collect(Collectors.toSet());

        return new ArrayList<>(permissionRepository.findAllById(ids));
    }
}
