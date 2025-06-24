package com.chien.identity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chien.identity.dto.request.RoleRequest;
import com.chien.identity.dto.response.RoleResponse;
import com.chien.identity.entity.Permission;
import com.chien.identity.exception.AppException;
import com.chien.identity.exception.ErrorCode;
import com.chien.identity.mapper.RoleMapper;
import com.chien.identity.repository.PermissionRepository;
import com.chien.identity.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsById(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        var role = roleMapper.toRole(request);

        var permissionIds = request.getPermissions();
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        var permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        role.setPermissions(permissions.stream().map(Permission::getName).toList());
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        if (!roleRepository.existsById(role)) {
            throw new AppException(ErrorCode.ROLE_NOTFOUND);
        }
        roleRepository.deleteById(role);
    }
}
