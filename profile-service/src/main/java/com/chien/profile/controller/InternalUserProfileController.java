package com.chien.profile.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.chien.profile.dto.request.ProfileCreationRequest;
import com.chien.profile.dto.response.UserProfileResponse;
import com.chien.profile.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    public UserProfileResponse createProfile(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody ProfileCreationRequest request) {
        String userId = jwt.getClaimAsString("userId");
        return userProfileService.createProfile(userId, request);
    }
}
