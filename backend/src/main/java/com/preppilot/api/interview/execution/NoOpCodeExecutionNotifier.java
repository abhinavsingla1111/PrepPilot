package com.preppilot.api.interview.execution;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(CodeExecutionNotifier.class)
public class NoOpCodeExecutionNotifier implements CodeExecutionNotifier {
    @Override
    public void notify(CodingExecutionStore.Completion completion) {
        // Results remain available in the authenticated Interview Lab history.
    }
}
