package com.preppilot.api.progress;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.problem.Difficulty;
import com.preppilot.api.problem.Problem;
import com.preppilot.api.problem.ProblemRepository;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ProgressService {

    private final UserProgressRepository progressRepository;
    private final ProblemRepository problemRepository;
    private final AppUserRepository userRepository;
    private final Clock clock;

    public ProgressService(
            UserProgressRepository progressRepository,
            ProblemRepository problemRepository,
            AppUserRepository userRepository,
            Clock clock
    ) {
        this.progressRepository = progressRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<ProgressResponse> findForUser(PrepPilotPrincipal principal) {
        return progressRepository.findAllByUserIdOrderByUpdatedAtDesc(principal.id()).stream()
                .map(ProgressResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DashboardSummary getSummary(PrepPilotPrincipal principal) {
        UUID userId = principal.id();
        long solved = progressRepository.countByUserIdAndStatus(userId, ProgressStatus.SOLVED);
        long totalProblems = problemRepository.count();
        Instant weekAgo = clock.instant().minus(7, ChronoUnit.DAYS);
        long solvedThisWeek = progressRepository
                .countByUserIdAndStatusAndSolvedAtAfter(userId, ProgressStatus.SOLVED, weekAgo);

        List<Long> solvedIds = progressRepository.findProblemIdsByStatus(userId, ProgressStatus.SOLVED);
        Pageable pool = PageRequest.of(0, 24);
        List<Problem> candidates = solvedIds.isEmpty()
                ? problemRepository.findRecommended(pool)
                : problemRepository.findRecommendedExcluding(solvedIds, pool);

        LinkedHashMap<Integer, Problem> byLeetcodeId = new LinkedHashMap<>();
        for (Problem problem : candidates) {
            byLeetcodeId.putIfAbsent(problem.getLeetcodeId(), problem);
        }
        List<RecommendedProblem> recommended = byLeetcodeId.values().stream()
                .limit(3)
                .map(RecommendedProblem::from)
                .toList();

        return new DashboardSummary(solved, totalProblems, solvedThisWeek, recommended);
    }

    @Transactional
    public ProgressResponse update(PrepPilotPrincipal principal, long problemId, ProgressStatus status) {
        Instant now = clock.instant();
        UserProgress progress = progressRepository.findByUserIdAndProblemId(principal.id(), problemId)
                .orElseGet(() -> {
                    AppUser user = userRepository.findById(principal.id())
                            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
                    Problem problem = problemRepository.findById(problemId)
                            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Problem not found."));
                    return new UserProgress(UUID.randomUUID(), user, problem, status, now);
                });
        progress.update(status, now);
        return ProgressResponse.from(progressRepository.save(progress));
    }

    public record ProgressResponse(long problemId, ProgressStatus status, Instant solvedAt) {
        static ProgressResponse from(UserProgress progress) {
            return new ProgressResponse(progress.getProblem().getId(), progress.getStatus(), progress.getSolvedAt());
        }
    }

    public record DashboardSummary(
            long solved,
            long totalProblems,
            long solvedThisWeek,
            List<RecommendedProblem> recommended
    ) {
    }

    public record RecommendedProblem(
            long id,
            int leetcodeId,
            String title,
            String url,
            Difficulty difficulty,
            String topic
    ) {
        static RecommendedProblem from(Problem problem) {
            return new RecommendedProblem(
                    problem.getId(),
                    problem.getLeetcodeId(),
                    problem.getTitle(),
                    problem.getUrl(),
                    problem.getDifficulty(),
                    problem.getTopic()
            );
        }
    }
}
