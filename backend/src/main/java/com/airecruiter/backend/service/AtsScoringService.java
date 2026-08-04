package com.airecruiter.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtsScoringService {

    public double calculateScore(List<String> candidateSkills, List<String> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }
        if (candidateSkills == null || candidateSkills.isEmpty()) {
            return 0.0;
        }

        long matchedCount = requiredSkills.stream()
                .filter(requiredSkill -> candidateSkills.stream()
                        .anyMatch(candidateSkill -> candidateSkill.equalsIgnoreCase(requiredSkill)))
                .count();

        return (matchedCount / (double) requiredSkills.size()) * 100;
    }
}