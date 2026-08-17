package com.preppilot.api.problem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "problem",
        uniqueConstraints = @UniqueConstraint(name = "uk_problem_leetcode_topic", columnNames = {"leetcode_id", "topic"})
)
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leetcode_id", nullable = false)
    private int leetcodeId;

    @Column(nullable = false, length = 180)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Difficulty difficulty;

    @Column(nullable = false, length = 120)
    private String topic;

    @Column(name = "learning_order", nullable = false)
    private int learningOrder;

    protected Problem() {
    }

    public Problem(int leetcodeId, String slug, String title, String url, Difficulty difficulty, String topic) {
        this.leetcodeId = leetcodeId;
        this.slug = slug;
        this.title = title;
        this.url = url;
        this.difficulty = difficulty;
        this.topic = topic;
        this.learningOrder = LearningPath.orderFor(topic, difficulty);
    }

    public Long getId() {
        return id;
    }

    public int getLeetcodeId() {
        return leetcodeId;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public int getLearningOrder() {
        return learningOrder;
    }

    void recomputeLearningOrder() {
        this.learningOrder = LearningPath.orderFor(this.topic, this.difficulty);
    }
}
