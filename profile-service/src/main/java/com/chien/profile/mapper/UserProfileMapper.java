package com.chien.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.chien.profile.dto.request.ProfileCreationRequest;
import com.chien.profile.dto.request.ProfileUpdateRequest;
import com.chien.profile.dto.response.UserProfileResponse;
import com.chien.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    void updateUserProfile(@MappingTarget UserProfile userProfile, ProfileUpdateRequest request);
}
