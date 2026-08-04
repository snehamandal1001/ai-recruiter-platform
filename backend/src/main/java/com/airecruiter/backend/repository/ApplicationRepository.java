package com.airecruiter.backend.repository;

import com.airecruiter.backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobPostingIdOrderByAtsScoreDesc(Long jobPostingId);
}