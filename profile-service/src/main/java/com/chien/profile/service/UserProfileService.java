package com.chien.profile.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chien.profile.dto.request.ProfileCreationRequest;
import com.chien.profile.dto.request.ProfileUpdateRequest;
import com.chien.profile.dto.response.UserProfileResponse;
import com.chien.profile.entity.UserProfile;
import com.chien.profile.mapper.UserProfileMapper;
import com.chien.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile.setCreatedAt(new Date());
        userProfile.setUpdatedAt(new Date());
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();
        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public UserProfileResponse updateProfile(String profileId, ProfileUpdateRequest request) {
        UserProfile userProfile =
                userProfileRepository.findById(profileId).orElseThrow(() -> new RuntimeException("Profile not found"));

        userProfileMapper.updateUserProfile(userProfile, request);

        userProfile.setUpdatedAt(new Date());

        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
