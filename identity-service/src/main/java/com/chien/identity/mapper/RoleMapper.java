package com.chien.identity.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.chien.identity.dto.request.RoleRequest;
import com.chien.identity.dto.response.PermissionResponse;
import com.chien.identity.dto.response.RoleResponse;
import com.chien.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    default Set<PermissionResponse> map(List<String> permissions) {
        if (permissions == null) return null;
        return permissions.stream().map(PermissionResponse::new).collect(Collectors.toSet());
    }
}
