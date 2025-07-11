package com.chien.profile.controller;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.chien.profile.dto.request.ProfileUpdateRequest;
import com.chien.profile.dto.response.UserProfileResponse;
import com.chien.profile.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @GetMapping("/users/me")
    public UserProfileResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        return userProfileService.getProfile(userId);
    }

    @GetMapping("/users")
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }

    @PutMapping("/users/me")
    public UserProfileResponse updateProfile(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody ProfileUpdateRequest request) {
        String userId = jwt.getClaimAsString("userId");
        return userProfileService.updateProfile(userId, request);
    }
}
