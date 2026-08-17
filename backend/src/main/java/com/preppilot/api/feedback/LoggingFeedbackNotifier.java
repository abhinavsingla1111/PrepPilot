package com.preppilot.api.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingFeedbackNotifier implements FeedbackNotifier {

    private static final Logger log = LoggerFactory.getLogger(LoggingFeedbackNotifier.class);

    @Override
    public FeedbackEmailStatus send(FeedbackRequest request) {
        log.info("Feedback notification skipped because email is disabled; requestId={}", request.getId());
        return FeedbackEmailStatus.SKIPPED;
    }
}
