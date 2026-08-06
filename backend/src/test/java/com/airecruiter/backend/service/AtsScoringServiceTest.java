package com.airecruiter.backend.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtsScoringServiceTest {

    private final AtsScoringService service = new AtsScoringService();

    @Test
    void shouldReturn100PercentWhenAllSkillsMatch() {
        double score = service.calculateScore(
                List.of("Java", "React", "Docker"),
                List.of("Java", "React", "Docker")
        );
        assertEquals(100.0, score);
    }

    @Test
    void shouldReturn50PercentWhenHalfSkillsMatch() {
        double score = service.calculateScore(
                List.of("Java", "Python"),
                List.of("Java", "React")
        );
        assertEquals(50.0, score);
    }

    @Test
    void shouldReturnZeroWhenNoSkillsMatch() {
        double score = service.calculateScore(
                List.of("Python", "Ruby"),
                List.of("Java", "React")
        );
        assertEquals(0.0, score);
    }

    @Test
    void shouldReturnZeroWhenRequiredSkillsIsEmpty() {
        double score = service.calculateScore(
                List.of("Java", "React"),
                List.of()
        );
        assertEquals(0.0, score);
    }

    @Test
    void shouldReturnZeroWhenCandidateHasNoSkills() {
        double score = service.calculateScore(
                List.of(),
                List.of("Java", "React")
        );
        assertEquals(0.0, score);
    }

    @Test
    void shouldMatchCaseInsensitively() {
        double score = service.calculateScore(
                List.of("java", "REACT"),
                List.of("Java", "React")
        );
        assertEquals(100.0, score);
    }
}