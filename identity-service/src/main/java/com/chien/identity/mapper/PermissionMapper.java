package com.chien.identity.mapper;

import org.mapstruct.Mapper;

import com.chien.identity.dto.request.PermissionRequest;
import com.chien.identity.dto.response.PermissionResponse;
import com.chien.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
