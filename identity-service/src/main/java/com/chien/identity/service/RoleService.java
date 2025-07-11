package com.chien.identity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chien.identity.dto.request.RoleRequest;
import com.chien.identity.dto.response.RoleResponse;
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
        role = roleRepository.save(role);

        var savedRole =
                roleRepository.findById(role.getName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTFOUND));
        return roleMapper.toRoleResponse(savedRole);
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
