package com.preppilot.api.interview.execution;

import com.preppilot.api.interview.CodingLanguage;

import java.util.List;

public interface CodeExecutionGateway {

    List<CaseOutcome> execute(CodingLanguage language, String sourceCode, List<ExecutionCase> cases);

    record ExecutionCase(String input, String expectedOutput, boolean visible) {}

    record CaseOutcome(
            int position,
            boolean visible,
            CodeExecutionCaseStatus status,
            String input,
            String expectedOutput,
            String actualOutput,
            String diagnostic,
            String timeSeconds,
            Integer memoryKilobytes
    ) {}
}
