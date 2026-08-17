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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cheatsheets")
public class CheatsheetController {

    private final Map<String, CheatsheetResponse> cheatsheets;
    private final Map<String, CheatsheetResponse> oopGuides;

    public CheatsheetController() {
        this.cheatsheets = Map.of(
                "java", new CheatsheetResponse("Java DSA Cheat Sheet", load("cheatsheets/java-dsa.md")),
                "python", new CheatsheetResponse("Python DSA Cheat Sheet", load("cheatsheets/python-dsa.md")),
                "cpp", new CheatsheetResponse("C++ DSA Cheat Sheet", load("cheatsheets/cpp-dsa.md"))
        );
        this.oopGuides = Map.of(
                "java", new CheatsheetResponse("Java OOP Concepts", load("cheatsheets/oop-java.md")),
                "python", new CheatsheetResponse("Python OOP Concepts", load("cheatsheets/oop-python.md")),
                "cpp", new CheatsheetResponse("C++ OOP Concepts", load("cheatsheets/oop-cpp.md"))
        );
    }

    @GetMapping("/{language}")
    CheatsheetResponse cheatsheet(@PathVariable String language) {
        return require(cheatsheets, language);
    }

    @GetMapping("/oop/{language}")
    CheatsheetResponse oop(@PathVariable String language) {
        return require(oopGuides, language);
    }

    private static CheatsheetResponse require(Map<String, CheatsheetResponse> source, String language) {
        CheatsheetResponse response = source.get(language.toLowerCase(Locale.ROOT));
        if (response == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "That guide is not available.");
        }
        return response;
    }

    private static String load(String path) {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load cheat sheet resource: " + path, e);
        }
    }

    record CheatsheetResponse(String title, String content) {
    }
}
