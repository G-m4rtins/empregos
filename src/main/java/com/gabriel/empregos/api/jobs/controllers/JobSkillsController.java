package com.gabriel.empregos.api.jobs.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.api.skills.mappers.SkillMapper;
import com.gabriel.empregos.core.exceptions.JobNotFoundException;
import com.gabriel.empregos.core.repositories.JobRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{id}/skills")
public class JobSkillsController {

    private final JobRepository jobRepository;
    private final SkillMapper jobSkillMapper;


    @GetMapping
    public List<SkillResponse> findSkillsByJobId(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow( () -> new JobNotFoundException(id) );

        return job.getSkills()
            .stream()
            .map(jobSkillMapper::toSkillResponse)
            .toList();
    }

}
