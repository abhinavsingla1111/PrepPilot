package com.preppilot.api.problem;

import com.preppilot.api.common.ApiException;
import com.preppilot.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public PageResponse<ProblemResponse> findProblems(
            String query,
            String difficultyValue,
            String topic,
            String statusValue,
            UUID userId,
            int page,
            int size
    ) {
        Difficulty difficulty = parseDifficulty(difficultyValue);
        Boolean solved = parseSolved(statusValue);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("learningOrder").ascending().and(Sort.by("leetcodeId").ascending()));
        Page<Problem> problems = problemRepository.findAll(
                ProblemSpecifications.titleOrTopicContains(query)
                        .and(ProblemSpecifications.hasDifficulty(difficulty))
                        .and(ProblemSpecifications.hasTopic(topic))
                        .and(ProblemSpecifications.solvedByUser(userId, solved)),
                pageRequest
        );
        return PageResponse.from(problems, ProblemResponse::from);
    }

    public List<String> findTopics() {
        return problemRepository.findDistinctTopics();
    }

    private Difficulty parseDifficulty(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Difficulty.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Difficulty must be EASY, MEDIUM, or HARD.");
        }
    }

    private Boolean parseSolved(String value) {
        if (value == null || value.isBlank()) return null;
        return switch (value.toUpperCase(Locale.ROOT)) {
            case "SOLVED" -> Boolean.TRUE;
            case "UNSOLVED" -> Boolean.FALSE;
            default -> throw new ApiException(HttpStatus.BAD_REQUEST, "Status must be SOLVED or UNSOLVED.");
        };
    }

    public record ProblemResponse(
            long id,
            int leetcodeId,
            String slug,
            String title,
            String url,
            Difficulty difficulty,
            String topic
    ) {
        static ProblemResponse from(Problem problem) {
            return new ProblemResponse(
                    problem.getId(),
                    problem.getLeetcodeId(),
                    problem.getSlug(),
                    problem.getTitle(),
                    problem.getUrl(),
                    problem.getDifficulty(),
                    problem.getTopic()
            );
        }
    }
}
