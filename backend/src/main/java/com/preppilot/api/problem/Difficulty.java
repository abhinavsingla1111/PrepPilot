package com.preppilot.api.problem;

public enum Difficulty {
    EASY(0),
    MEDIUM(1),
    HARD(2);

    private final int rank;

    Difficulty(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }
}
