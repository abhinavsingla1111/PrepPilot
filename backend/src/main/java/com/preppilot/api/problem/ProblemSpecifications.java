package com.preppilot.api.problem;

import com.preppilot.api.progress.ProgressStatus;
import com.preppilot.api.progress.UserProgress;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.UUID;

final class ProblemSpecifications {

    private ProblemSpecifications() {
    }

    static Specification<Problem> titleOrTopicContains(String query) {
        return (root, criteriaQuery, builder) -> {
            if (query == null || query.isBlank()) return builder.conjunction();
            String pattern = "%" + query.strip().toLowerCase(Locale.ROOT) + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("title")), pattern),
                    builder.like(builder.lower(root.get("topic")), pattern)
            );
        };
    }

    static Specification<Problem> hasDifficulty(Difficulty difficulty) {
        return (root, criteriaQuery, builder) ->
                difficulty == null ? builder.conjunction() : builder.equal(root.get("difficulty"), difficulty);
    }

    static Specification<Problem> hasTopic(String topic) {
        return (root, criteriaQuery, builder) ->
                topic == null || topic.isBlank() ? builder.conjunction() : builder.equal(root.get("topic"), topic);
    }

    static Specification<Problem> solvedByUser(UUID userId, Boolean solved) {
        return (root, criteriaQuery, builder) -> {
            if (userId == null || solved == null) return builder.conjunction();
            Subquery<UUID> subquery = criteriaQuery.subquery(UUID.class);
            Root<UserProgress> progress = subquery.from(UserProgress.class);
            subquery.select(progress.get("id"));
            subquery.where(
                    builder.equal(progress.get("user").get("id"), userId),
                    builder.equal(progress.get("problem").get("id"), root.get("id")),
                    builder.equal(progress.get("status"), ProgressStatus.SOLVED)
            );
            return solved ? builder.exists(subquery) : builder.not(builder.exists(subquery));
        };
    }
}
