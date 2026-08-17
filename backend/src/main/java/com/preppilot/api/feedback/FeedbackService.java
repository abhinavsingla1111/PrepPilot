package com.preppilot.api.feedback;

import com.preppilot.api.common.ApiException;
import com.preppilot.api.feedback.FeedbackImageValidator.ValidatedImage;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.util.UUID;

@Service
public class FeedbackService {

    private final FeedbackRequestRepository repository;
    private final AppUserRepository userRepository;
    private final FeedbackImageValidator imageValidator;
    private final FeedbackNotifier notifier;
    private final Clock clock;

    public FeedbackService(
            FeedbackRequestRepository repository,
            AppUserRepository userRepository,
            FeedbackImageValidator imageValidator,
            FeedbackNotifier notifier,
            Clock clock
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.imageValidator = imageValidator;
        this.notifier = notifier;
        this.clock = clock;
    }

    @Transactional
    public FeedbackResponse create(
            UUID userId,
            FeedbackType type,
            FeedbackSeverity severity,
            String subject,
            String description,
            MultipartFile image
    ) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in to continue."));
        String cleanSubject = subject.trim();
        String cleanDescription = description.trim();
        ValidatedImage validatedImage = imageValidator.validate(image);

        FeedbackRequest request = new FeedbackRequest(
                UUID.randomUUID(), user, type, severity, cleanSubject, cleanDescription, clock.instant()
        );
        if (validatedImage != null) {
            request.attach(validatedImage.name(), validatedImage.contentType(), validatedImage.data());
        }

        request = repository.save(request);
        request.markEmailStatus(notifier.send(request));
        request = repository.save(request);
        return FeedbackResponse.from(request);
    }

    public record FeedbackResponse(
            UUID id,
            FeedbackStatus status,
            FeedbackEmailStatus emailStatus,
            java.time.Instant createdAt
    ) {
        static FeedbackResponse from(FeedbackRequest request) {
            return new FeedbackResponse(
                    request.getId(), request.getStatus(), request.getEmailStatus(), request.getCreatedAt()
            );
        }
    }
}
