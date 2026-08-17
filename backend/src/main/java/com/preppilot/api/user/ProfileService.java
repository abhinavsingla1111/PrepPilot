package com.preppilot.api.user;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class ProfileService {

    private final AppUserRepository userRepository;
    private final Clock clock;

    public ProfileService(AppUserRepository userRepository, Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(PrepPilotPrincipal principal) {
        return ProfileResponse.from(requireUser(principal));
    }

    @Transactional
    public ProfileResponse updateProfile(
            PrepPilotPrincipal principal,
            String fullName,
            Integer age,
            String mobile,
            String avatarUrl
    ) {
        AppUser user = requireUser(principal);
        user.updateProfile(
                normalize(fullName),
                age,
                normalize(mobile),
                normalize(avatarUrl),
                clock.instant()
        );
        return ProfileResponse.from(userRepository.save(user));
    }

    private static String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private AppUser requireUser(PrepPilotPrincipal principal) {
        return userRepository.findById(principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
    }

    public record ProfileResponse(
            String email,
            String leetcodeUsername,
            String fullName,
            Integer age,
            String mobile,
            String avatarUrl
    ) {
        static ProfileResponse from(AppUser user) {
            return new ProfileResponse(
                    user.getEmail(),
                    user.getLeetcodeUsername(),
                    user.getFullName(),
                    user.getAge(),
                    user.getMobile(),
                    user.getAvatarUrl()
            );
        }
    }
}
