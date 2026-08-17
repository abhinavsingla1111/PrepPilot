package com.preppilot.api.problem;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ProblemSeedService {

    private static final Logger log = LoggerFactory.getLogger(ProblemSeedService.class);
    private static final String DATASET = "leetcode_interview_crash_course_dsa_questions.csv";

    private final ProblemRepository problemRepository;

    public ProblemSeedService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Transactional
    public void seedIfEmpty() {
        if (problemRepository.count() > 0) return;

        ClassPathResource resource = new ClassPathResource(DATASET);
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader("Problem Name", "LeetCode ID", "Difficulty", "Topic", "LeetCode Link")
                     .setSkipHeaderRecord(true)
                     .get()
                     .parse(reader)) {
            List<Problem> problems = new ArrayList<>();
            for (CSVRecord record : parser) {
                String url = record.get("LeetCode Link").strip();
                problems.add(new Problem(
                        Integer.parseInt(record.get("LeetCode ID").strip()),
                        extractSlug(url),
                        record.get("Problem Name").strip(),
                        url,
                        Difficulty.valueOf(record.get("Difficulty").strip().toUpperCase(Locale.ROOT)),
                        record.get("Topic").strip()
                ));
            }
            problemRepository.saveAll(problems);
            log.info("Seeded {} curated DSA questions", problems.size());
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load the curated problem dataset", exception);
        }
    }

    /**
     * Recomputes the topic-wise learning-path order for every problem. Runs on every
     * startup so already-seeded rows (and any future ranking changes) are kept in sync.
     */
    @Transactional
    public void refreshLearningOrder() {
        List<Problem> problems = problemRepository.findAll();
        problems.forEach(Problem::recomputeLearningOrder);
        problemRepository.saveAll(problems);
    }

    private String extractSlug(String url) {
        String path = URI.create(url).getPath();
        String prefix = "/problems/";
        int start = path.indexOf(prefix);
        if (start < 0) throw new IllegalArgumentException("Unexpected LeetCode URL: " + url);
        String remainder = path.substring(start + prefix.length());
        return remainder.endsWith("/") ? remainder.substring(0, remainder.length() - 1) : remainder;
    }
}
