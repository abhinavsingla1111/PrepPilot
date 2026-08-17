package com.preppilot.api.user;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    ProfileService.ProfileResponse getProfile(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return profileService.getProfile(principal);
    }

    @PutMapping
    ProfileService.ProfileResponse updateProfile(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return profileService.updateProfile(
                principal,
                request.fullName(),
                request.age(),
                request.mobile(),
                request.avatarUrl()
        );
    }

    public record UpdateProfileRequest(
            @Size(max = 80, message = "Name is too long.")
            String fullName,

            @Min(value = 1, message = "Enter a valid age.")
            @Max(value = 120, message = "Enter a valid age.")
            Integer age,

            @Pattern(regexp = "^$|^[1-9]\\d{9}$", message = "Enter a valid 10-digit mobile number that does not start with 0.")
            String mobile,

            @Size(max = 700_000, message = "Profile picture is too large.")
            String avatarUrl
    ) {
    }
}
