package com.preppilot.api.interview.execution;

public interface CodeExecutionNotifier {
    void notify(CodingExecutionStore.Completion completion);
}
