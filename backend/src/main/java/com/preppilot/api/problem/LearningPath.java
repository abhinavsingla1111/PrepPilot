package com.preppilot.api.problem;

import java.util.Locale;
import java.util.Map;

/**
 * Defines the topic-wise learning progression used to order the question library.
 * Questions are grouped by data-structure / concept in the order below, and within
 * each concept they are ordered by difficulty (Easy -> Medium -> Hard). This lets a
 * learner move through one topic at a time from easiest to hardest.
 */
final class LearningPath {

    private static final Map<String, Integer> TOPIC_RANK = Map.ofEntries(
            Map.entry("math", 0),
            Map.entry("bit manipulation", 1),
            Map.entry("arrays & strings", 2),
            Map.entry("strings", 3),
            Map.entry("two pointers", 4),
            Map.entry("binary search", 5),
            Map.entry("linked list", 6),
            Map.entry("trees & graphs", 7),
            Map.entry("trie", 8),
            Map.entry("stacks & queues", 9),
            Map.entry("hashing", 10),
            Map.entry("heap", 11),
            Map.entry("greedy", 12),
            Map.entry("backtracking", 13),
            Map.entry("dynamic programming", 14),
            Map.entry("matrix", 15),
            Map.entry("advanced graphs", 16),
            Map.entry("design", 17)
    );

    private static final int UNKNOWN_TOPIC_RANK = 999;

    private LearningPath() {
    }

    static int topicRank(String topic) {
        if (topic == null) {
            return UNKNOWN_TOPIC_RANK;
        }
        return TOPIC_RANK.getOrDefault(topic.strip().toLowerCase(Locale.ROOT), UNKNOWN_TOPIC_RANK);
    }

    static int orderFor(String topic, Difficulty difficulty) {
        return topicRank(topic) * 10 + difficulty.rank();
    }
}
