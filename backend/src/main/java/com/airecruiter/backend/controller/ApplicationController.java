package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.Application;
import com.airecruiter.backend.model.Candidate;
import com.airecruiter.backend.model.JobPosting;
import com.airecruiter.backend.repository.ApplicationRepository;
import com.airecruiter.backend.repository.CandidateRepository;
import com.airecruiter.backend.repository.JobPostingRepository;
import com.airecruiter.backend.service.AtsScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private AtsScoringService atsScoringService;

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
}