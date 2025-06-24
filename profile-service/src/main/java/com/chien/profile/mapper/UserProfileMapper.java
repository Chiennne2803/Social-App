package com.chien.profile.mapper;

import com.chien.profile.dto.request.ProfileCreationRequest;
import com.chien.profile.dto.response.UserProfileResponse;
import com.chien.profile.entity.UserProfile;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);
}
