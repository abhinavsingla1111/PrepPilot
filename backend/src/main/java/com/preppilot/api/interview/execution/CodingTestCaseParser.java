package com.preppilot.api.interview.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preppilot.api.interview.CodingInterviewPrompt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CodingTestCaseParser {

    private final ObjectMapper objectMapper;

    public CodingTestCaseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<CodeExecutionGateway.ExecutionCase> parse(CodingInterviewPrompt prompt) {
        List<CodeExecutionGateway.ExecutionCase> cases = new ArrayList<>();
        read(prompt.getPublicTestsJson()).forEach(test -> cases.add(
                new CodeExecutionGateway.ExecutionCase(test.input(), test.expectedOutput(), true)
        ));
        read(prompt.getHiddenTestsJson()).forEach(test -> cases.add(
                new CodeExecutionGateway.ExecutionCase(test.input(), test.expectedOutput(), false)
        ));
        return List.copyOf(cases);
    }

    public List<PublicExample> publicExamples(CodingInterviewPrompt prompt) {
        return read(prompt.getPublicTestsJson()).stream()
                .map(test -> new PublicExample(test.input(), test.expectedOutput(), test.explanation()))
                .toList();
    }

    private List<StoredTestCase> read(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (IOException exception) {
            throw new UncheckedIOException("Coding interview test data is invalid.", exception);
        }
    }

    private record StoredTestCase(String input, String expectedOutput, String explanation) {}

    public record PublicExample(String input, String expectedOutput, String explanation) {}
}
