package com.gabriel.empregos.api.jobs.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.jobs.mappers.JobMapper;
import com.gabriel.empregos.core.exceptions.JobNotFoundException;
import com.gabriel.empregos.core.repositories.JobRepository;
import com.gabriel.empregos.core.services.auth.AuthenticatedUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{id}")
public class CandidatesRestControllers {

    private final JobRepository jobRepository;

    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<?> apply(@PathVariable Long id, Authentication authentication) {
        var job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
        var user = (AuthenticatedUser) authentication.getPrincipal();
        job.getCandidates().add(user.getUser());
        jobRepository.save(job);
        return ResponseEntity.noContent().build();
    }

}
