package com.preppilot.api.interview;

public enum CodingLanguage {
    JAVA("Java"),
    PYTHON("Python"),
    CPP("C++");

    private final String displayName;

    CodingLanguage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
