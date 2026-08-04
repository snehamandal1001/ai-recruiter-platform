package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.Application;
import com.airecruiter.backend.model.Candidate;
import com.airecruiter.backend.model.JobPosting;
import com.airecruiter.backend.repository.ApplicationRepository;
import com.airecruiter.backend.repository.CandidateRepository;
import com.airecruiter.backend.repository.JobPostingRepository;
import com.airecruiter.backend.service.AtsScoringService;
import com.airecruiter.backend.service.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private AtsScoringService atsScoringService;

    @Autowired
    private LlmService llmService;

    @PostMapping("/apply")
    public Application apply(@RequestParam Long candidateId, @RequestParam Long jobId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        double score = atsScoringService.calculateScore(
                candidate.getExtractedSkills(),
                job.getRequiredSkills()
        );

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJobPosting(job);
        application.setAtsScore(score);

        return applicationRepository.save(application);
    }

    @GetMapping("/job/{jobId}/ranked")
    public List<Application> getRankedCandidates(@PathVariable Long jobId) {
        return applicationRepository.findByJobPostingIdOrderByAtsScoreDesc(jobId);
    }

    @GetMapping("/{applicationId}/interview-questions")
    public List<String> getInterviewQuestions(@PathVariable Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return llmService.generateInterviewQuestions(
                application.getJobPosting().getTitle(),
                application.getJobPosting().getDescription(),
                application.getCandidate().getExtractedSkills()
        );
    }
}