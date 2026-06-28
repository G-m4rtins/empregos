package com.gabriel.empregos.api.jobs.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.jobs.dtos.JobResponse;
import com.gabriel.empregos.api.jobs.mappers.JobMapper;
import com.gabriel.empregos.core.repositories.JobRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobRestController {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @GetMapping
    public List<JobResponse> findAll() {
        var jobs = jobRepository.findAll()
        .stream()
        .map(jobMapper::toJobResponse)
        .toList();

        return jobs;
    }

}
