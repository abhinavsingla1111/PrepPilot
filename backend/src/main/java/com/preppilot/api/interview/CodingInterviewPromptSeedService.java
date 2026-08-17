package com.preppilot.api.interview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preppilot.api.problem.Difficulty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CodingInterviewPromptSeedService {

    public static final int PROMPT_COUNT = 20;
    private static final String SEED_PATH = "interview/coding-prompts.json";

    private final CodingInterviewPromptRepository promptRepository;
    private final ObjectMapper objectMapper;

    public CodingInterviewPromptSeedService(
            CodingInterviewPromptRepository promptRepository,
            ObjectMapper objectMapper
    ) {
        this.promptRepository = promptRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void seedIfEmpty() {
        List<SeedRow> rows = readSeed();
        validate(rows);
        promptRepository.deactivateAll();

        for (SeedRow row : rows) {
            CodingInterviewPrompt prompt = promptRepository.findBySlug(row.slug())
                    .orElseGet(() -> new CodingInterviewPrompt(row.slug(), row.learningOrder()));
            prompt.update(new CodingInterviewPrompt.SeedPrompt(
                    row.title(), row.difficulty(), row.topic(), row.description(), row.constraints(), row.examples(),
                    row.expectedApproach(), row.expectedTimeComplexity(), row.expectedSpaceComplexity(),
                    row.learningOrder(), row.companyTags(), row.inputFormat(), row.outputFormat(),
                    row.starters().java(), row.starters().cpp(), row.starters().python(),
                    writeTests(row.publicTests()), writeTests(row.hiddenTests())
            ));
            promptRepository.save(prompt);
        }
    }

    private List<SeedRow> readSeed() {
        try (var input = new ClassPathResource(SEED_PATH).getInputStream()) {
            return objectMapper.readValue(input, new TypeReference<>() {});
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not load coding interview prompt seed.", exception);
        }
    }

    private String writeTests(List<CodingTestCase> tests) {
        try {
            return objectMapper.writeValueAsString(tests);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not serialize coding interview tests.", exception);
        }
    }

    private static void validate(List<SeedRow> rows) {
        if (rows.size() != PROMPT_COUNT) {
            throw new IllegalStateException("Expected " + PROMPT_COUNT + " coding prompts, found " + rows.size());
        }
        Set<String> slugs = new HashSet<>();
        Set<Integer> orders = new HashSet<>();
        for (SeedRow row : rows) {
            if (!slugs.add(row.slug()) || !orders.add(row.learningOrder())) {
                throw new IllegalStateException("Coding prompt slugs and learning orders must be unique.");
            }
            if (row.publicTests().size() < 2 || row.hiddenTests().size() < 3) {
                throw new IllegalStateException("Each coding prompt needs at least two examples and three hidden tests: " + row.slug());
            }
            if (row.starters().java().isBlank() || row.starters().cpp().isBlank() || row.starters().python().isBlank()) {
                throw new IllegalStateException("Each coding prompt needs Java, C++, and Python starter code: " + row.slug());
            }
        }
    }

    public record CodingTestCase(String input, String expectedOutput, String explanation) {}

    private record Starters(String java, String cpp, String python) {}

    private record SeedRow(
            String slug,
            String title,
            Difficulty difficulty,
            String topic,
            int learningOrder,
            String companyTags,
            String description,
            String constraints,
            String inputFormat,
            String outputFormat,
            String examples,
            String expectedApproach,
            String expectedTimeComplexity,
            String expectedSpaceComplexity,
            Starters starters,
            List<CodingTestCase> publicTests,
            List<CodingTestCase> hiddenTests
    ) {}
}
