package com.preppilot.api.interview.execution;

public class CodeRunnerException extends RuntimeException {
    public CodeRunnerException(String message) {
        super(message);
    }

    public CodeRunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
