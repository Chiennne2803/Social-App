package com.chien.identity.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.chien.identity.dto.request.UserCreationRequest;
import com.chien.identity.dto.request.UserUpdateRequest;
import com.chien.identity.dto.response.RoleResponse;
import com.chien.identity.dto.response.UserResponse;
import com.chien.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    @Mapping(source = "roles", target = "roles")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default Set<RoleResponse> map(List<String> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(role -> RoleResponse.builder()
                        .name(role)
                        .description(null)
                        .permissions(null)
                        .build())
                .collect(Collectors.toSet());
    }
}
