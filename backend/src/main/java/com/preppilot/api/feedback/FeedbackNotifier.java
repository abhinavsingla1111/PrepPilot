package com.preppilot.api.feedback;

public interface FeedbackNotifier {
    FeedbackEmailStatus send(FeedbackRequest request);
}
