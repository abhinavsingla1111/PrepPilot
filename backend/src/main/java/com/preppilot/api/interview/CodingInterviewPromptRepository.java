package com.preppilot.api.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CodingInterviewPromptRepository extends JpaRepository<CodingInterviewPrompt, Long> {
    Optional<CodingInterviewPrompt> findBySlug(String slug);
    List<CodingInterviewPrompt> findAllByActiveTrueOrderByLearningOrderAsc();
    List<CodingInterviewPrompt> findAllByActiveTrueAndIdNotInOrderByLearningOrderAsc(Collection<Long> excludedIds);

    @Modifying
    @Query("update CodingInterviewPrompt prompt set prompt.active = false")
    void deactivateAll();
}
