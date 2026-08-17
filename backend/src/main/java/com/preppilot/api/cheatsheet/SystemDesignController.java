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
@RequestMapping("/api/v1/systemdesign")
public class SystemDesignController {

    private static final Pattern SAFE_ID = Pattern.compile("^[a-z0-9-]{1,60}$");

    // Cache loaded documents so we only read each resource once.
    private final ConcurrentHashMap<String, SystemDesignResponse> cache = new ConcurrentHashMap<>();

    @GetMapping("/{docId}")
    SystemDesignResponse document(@PathVariable String docId) {
        String id = docId.toLowerCase(Locale.ROOT);
        if (!SAFE_ID.matcher(id).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid document id.");
        }
        return cache.computeIfAbsent(id, SystemDesignController::load);
    }

    private static SystemDesignResponse load(String id) {
        ClassPathResource resource = new ClassPathResource("systemdesign/" + id + ".md");
        if (!resource.exists()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "That document is not available.");
        }
        try (InputStream in = resource.getInputStream()) {
            return new SystemDesignResponse(id, StreamUtils.copyToString(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not load the document.");
        }
    }

    record SystemDesignResponse(String id, String content) {
    }
}
