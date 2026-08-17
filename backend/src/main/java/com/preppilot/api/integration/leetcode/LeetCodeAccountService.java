package com.preppilot.api.integration.leetcode;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.problem.Problem;
import com.preppilot.api.problem.ProblemRepository;
import com.preppilot.api.progress.ProgressStatus;
import com.preppilot.api.progress.UserProgress;
import com.preppilot.api.progress.UserProgressRepository;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LeetCodeAccountService {

    private static final int RECENT_LIMIT = 20;

    private final AppUserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserProgressRepository progressRepository;
    private final LeetCodeSyncService leetCodeSyncService;
    private final Clock clock;

    public LeetCodeAccountService(
            AppUserRepository userRepository,
            ProblemRepository problemRepository,
            UserProgressRepository progressRepository,
            LeetCodeSyncService leetCodeSyncService,
            Clock clock
    ) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.progressRepository = progressRepository;
        this.leetCodeSyncService = leetCodeSyncService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public LeetCodeStatus getStatus(PrepPilotPrincipal principal) {
        AppUser user = requireUser(principal);
        String username = user.getLeetcodeUsername();
        if (username == null) {
            return LeetCodeStatus.unlinked();
        }
        LeetCodeSyncService.SolvedStats stats = safeStats(username);
        return new LeetCodeStatus(username, Stats.fromNullable(stats), 0);
    }

    @Transactional
    public LeetCodeStatus link(PrepPilotPrincipal principal, String rawUsername) {
        String username = rawUsername == null ? "" : rawUsername.trim();
        AppUser user = requireUser(principal);
        // Validate the username against LeetCode before saving; throws if not found or unavailable.
        LeetCodeSyncService.SolvedStats stats = leetCodeSyncService.getSolvedStats(username);
        user.linkLeetcode(username, clock.instant());
        userRepository.save(user);
        int synced = syncSolvedProblems(user, username);
        return new LeetCodeStatus(username, Stats.from(stats), synced);
    }

    @Transactional
    public LeetCodeStatus sync(PrepPilotPrincipal principal) {
        AppUser user = requireUser(principal);
        String username = user.getLeetcodeUsername();
        if (username == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Link a LeetCode account first.");
        }
        LeetCodeSyncService.SolvedStats stats = leetCodeSyncService.getSolvedStats(username);
        int synced = syncSolvedProblems(user, username);
        return new LeetCodeStatus(username, Stats.from(stats), synced);
    }

    @Transactional
    public LeetCodeStatus unlink(PrepPilotPrincipal principal) {
        AppUser user = requireUser(principal);
        user.unlinkLeetcode(clock.instant());
        userRepository.save(user);
        return LeetCodeStatus.unlinked();
    }

    /**
     * Matches the user's recently-accepted LeetCode submissions against the PrepPilot problem
     * library (by slug) and marks those problems SOLVED. Returns how many were newly marked.
     */
    private int syncSolvedProblems(AppUser user, String username) {
        List<LeetCodeSyncService.RecentAcceptedResponse> recent =
                leetCodeSyncService.findRecentAccepted(username, RECENT_LIMIT);
        if (recent.isEmpty()) {
            return 0;
        }

        Map<String, Instant> acceptedBySlug = new HashMap<>();
        for (LeetCodeSyncService.RecentAcceptedResponse submission : recent) {
            acceptedBySlug.merge(
                    submission.slug(),
                    submission.acceptedAt(),
                    (existing, candidate) -> candidate.isAfter(existing) ? candidate : existing
            );
        }

        List<Problem> problems = problemRepository.findAllBySlugIn(acceptedBySlug.keySet());
        Instant now = clock.instant();
        int newlySolved = 0;

        for (Problem problem : problems) {
            UserProgress progress = progressRepository
                    .findByUserIdAndProblemId(user.getId(), problem.getId())
                    .orElse(null);
            if (progress == null) {
                progress = new UserProgress(UUID.randomUUID(), user, problem, ProgressStatus.SOLVED, now);
                progressRepository.save(progress);
                newlySolved++;
            } else if (progress.getStatus() != ProgressStatus.SOLVED) {
                progress.update(ProgressStatus.SOLVED, now);
                progressRepository.save(progress);
                newlySolved++;
            }
        }
        return newlySolved;
    }

    private LeetCodeSyncService.SolvedStats safeStats(String username) {
        try {
            return leetCodeSyncService.getSolvedStats(username);
        } catch (ApiException exception) {
            return null;
        }
    }

    private AppUser requireUser(PrepPilotPrincipal principal) {
        return userRepository.findById(principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
    }

    public record LeetCodeStatus(String username, Stats stats, int syncedSolved) {
        static LeetCodeStatus unlinked() {
            return new LeetCodeStatus(null, null, 0);
        }
    }

    public record Stats(int total, int easy, int medium, int hard) {
        static Stats from(LeetCodeSyncService.SolvedStats stats) {
            return new Stats(stats.total(), stats.easy(), stats.medium(), stats.hard());
        }

        static Stats fromNullable(LeetCodeSyncService.SolvedStats stats) {
            return stats == null ? null : from(stats);
        }
    }
}
