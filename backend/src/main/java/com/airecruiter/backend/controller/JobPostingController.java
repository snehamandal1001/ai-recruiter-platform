package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.JobPosting;
import com.airecruiter.backend.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @PostMapping
    public JobPosting createJob(@RequestBody JobPosting job) {
        return jobPostingRepository.save(job);
    }

    @GetMapping
    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }
}