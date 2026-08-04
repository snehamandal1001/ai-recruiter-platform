package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.Candidate;
import com.airecruiter.backend.repository.CandidateRepository;
import com.airecruiter.backend.service.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private LlmService llmService;

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        // Call the LLM to extract skills from the resume text they submitted
        List<String> skills = llmService.extractSkills(candidate.getResumeText());
        candidate.setExtractedSkills(skills);
        return candidateRepository.save(candidate);
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
}