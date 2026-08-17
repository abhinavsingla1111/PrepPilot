package com.preppilot.api.cheatsheet;

import com.preppilot.api.common.ApiException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/fundamentals")
public class FundamentalsController {

    private static final Pattern SAFE_ID = Pattern.compile("^[a-z0-9-]{1,60}$");

    // Cache loaded documents so we only read each resource once.
    private final ConcurrentHashMap<String, FundamentalsResponse> cache = new ConcurrentHashMap<>();

    @GetMapping("/{docId}")
    FundamentalsResponse document(@PathVariable String docId) {
        String id = docId.toLowerCase(Locale.ROOT);
        if (!SAFE_ID.matcher(id).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid document id.");
        }
        return cache.computeIfAbsent(id, FundamentalsController::load);
    }

    private static FundamentalsResponse load(String id) {
        ClassPathResource resource = new ClassPathResource("fundamentals/" + id + ".md");
        if (!resource.exists()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "That document is not available.");
        }
        try (InputStream in = resource.getInputStream()) {
            return new FundamentalsResponse(id, StreamUtils.copyToString(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not load the document.");
        }
    }

    record FundamentalsResponse(String id, String content) {
    }
}
