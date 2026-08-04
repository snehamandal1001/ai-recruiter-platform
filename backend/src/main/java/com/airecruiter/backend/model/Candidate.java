package com.airecruiter.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String resumeText;

    private LocalDateTime createdAt = LocalDateTime.now();
}