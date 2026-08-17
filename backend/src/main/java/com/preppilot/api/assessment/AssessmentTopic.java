package com.preppilot.api.assessment;

import com.preppilot.api.common.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum AssessmentTopic {
    OOPS("oops", "Object-Oriented Programming", 10),
    JAVA("java", "Java", 15),
    SPRING_BOOT("spring-boot", "Spring Boot", 12),
    REACT("react", "React", 10),
    CPP("cpp", "C++", 15),
    PYTHON("python", "Python", 12),
    COMPUTER_NETWORKS("computer-networks", "Computer Networks", 10),
    MYSQL("mysql", "MySQL", 12),
    OPERATING_SYSTEMS("operating-systems", "Operating Systems", 10);

    private final String slug;
    private final String displayName;
    private final int durationMinutes;

    AssessmentTopic(String slug, String displayName, int durationMinutes) {
        this.slug = slug;
        this.displayName = displayName;
        this.durationMinutes = durationMinutes;
    }

    public String getSlug() {
        return slug;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public static AssessmentTopic fromSlug(String slug) {
        return Arrays.stream(values())
                .filter(topic -> topic.slug.equalsIgnoreCase(slug))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Knowledge-check topic not found."));
    }
}
