package com.chien.identity.mapper;

import org.mapstruct.Mapper;

import com.chien.identity.dto.request.ProfileCreationRequest;
import com.chien.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
