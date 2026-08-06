package com.airecruiter.backend.controller;

import com.airecruiter.backend.model.JobPosting;
import com.airecruiter.backend.repository.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostingController.class)
class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobPostingRepository jobPostingRepository;

    @Test
    void shouldReturnListOfJobs() throws Exception {
        JobPosting job = new JobPosting();
        job.setId(1L);
        job.setTitle("Backend Developer");
        job.setDescription("Java role");

        when(jobPostingRepository.findAll()).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Backend Developer"));
    }
}