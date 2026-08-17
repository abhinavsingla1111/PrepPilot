package com.preppilot.api.problem;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long>, JpaSpecificationExecutor<Problem> {

    @Query("select distinct p.topic from Problem p order by p.topic")
    List<String> findDistinctTopics();

    List<Problem> findAllBySlugIn(Collection<String> slugs);

    @Query("select p from Problem p order by p.learningOrder asc, p.leetcodeId asc")
    List<Problem> findRecommended(Pageable pageable);

    @Query("select p from Problem p where p.id not in :excluded order by p.learningOrder asc, p.leetcodeId asc")
    List<Problem> findRecommendedExcluding(Collection<Long> excluded, Pageable pageable);
}
