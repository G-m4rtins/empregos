package com.gabriel.empregos.api.jobs.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import com.gabriel.empregos.core.permissions.empregosPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.core.exceptions.JobNotFoundException;
import com.gabriel.empregos.core.repositories.JobRepository;
import com.gabriel.empregos.core.services.auth.SecurityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{id}")
public class CandidatesRestControllers {

    private final JobRepository jobRepository;
    private final SecurityService securityService;

    @PostMapping("/apply")
    @empregosPermissions.IsCandidate
    public ResponseEntity<?> apply(@PathVariable Long id) {
        var job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
        job.getCandidates().add(securityService.getCurrentUser());
        jobRepository.save(job);
        return ResponseEntity.noContent().build();
    }

}
