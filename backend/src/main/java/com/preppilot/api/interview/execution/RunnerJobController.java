package com.preppilot.api.interview.execution;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/internal/runner/jobs")
public class RunnerJobController {

    private static final String LEASE_HEADER = "X-Runner-Lease";
    private final CodingExecutionStore store;

    public RunnerJobController(CodingExecutionStore store) {
        this.store = store;
    }

    @ModelAttribute
    void preventCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @PostMapping("/claim")
    ResponseEntity<CodingExecutionStore.Work> claim() {
        CodingExecutionStore.Work work = store.claim();
        if (work == null) return ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(work);
    }

    @PostMapping("/{submissionId}/complete")
    CodingExecutionStore.Completion complete(
            @PathVariable UUID submissionId,
            @RequestHeader(LEASE_HEADER) @Size(min = 32, max = 100) String leaseToken,
            @Valid @RequestBody CompleteRequest request
    ) {
        return store.complete(submissionId, leaseToken, request.results().stream().map(ResultRequest::toDomain).toList());
    }

    @PostMapping("/{submissionId}/fail")
    ResponseEntity<Void> fail(
            @PathVariable UUID submissionId,
            @RequestHeader(LEASE_HEADER) @Size(min = 32, max = 100) String leaseToken,
            @Valid @RequestBody FailRequest request
    ) {
        store.fail(submissionId, leaseToken, request.message());
        return ResponseEntity.noContent().build();
    }

    public record CompleteRequest(
            @NotEmpty @Size(max = 20) List<@Valid ResultRequest> results
    ) {}

    public record ResultRequest(
            @Min(1) @Max(20) int position,
            @NotNull CodingExecutionStore.WorkerCaseStatus status,
            @Size(max = 64_000) String actualOutput,
            @Size(max = 16_000) String diagnostic,
            @Pattern(regexp = "^(|[0-9]{1,6}(\\.[0-9]{1,6})?)$") String timeSeconds,
            @Min(0) @Max(1_000_000) Integer memoryKilobytes
    ) {
        CodingExecutionStore.WorkerCaseResult toDomain() {
            return new CodingExecutionStore.WorkerCaseResult(
                    position, status, actualOutput, diagnostic, timeSeconds, memoryKilobytes
            );
        }
    }

    public record FailRequest(
            @NotBlank @Size(max = 500) String message
    ) {}
}
