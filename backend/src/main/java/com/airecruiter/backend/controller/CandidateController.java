package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.Candidate;
import com.airecruiter.backend.repository.CandidateRepository;
import com.airecruiter.backend.service.LlmService;
import com.airecruiter.backend.service.ResumeParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private LlmService llmService;

    @Autowired
    private ResumeParserService resumeParserService;

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        List<String> skills = llmService.extractSkills(candidate.getResumeText());
        candidate.setExtractedSkills(skills);
        return candidateRepository.save(candidate);
    }

    @PostMapping("/upload")
    public Candidate uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email) throws Exception {

        String resumeText = resumeParserService.extractText(file);
        List<String> skills = llmService.extractSkills(resumeText);

        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setEmail(email);
        candidate.setResumeText(resumeText);
        candidate.setExtractedSkills(skills);

        return candidateRepository.save(candidate);
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
}