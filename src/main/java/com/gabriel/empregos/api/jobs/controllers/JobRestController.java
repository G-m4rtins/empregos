package com.gabriel.empregos.api.jobs.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.jobs.dtos.JobRequest;
import com.gabriel.empregos.api.jobs.dtos.JobResponse;
import com.gabriel.empregos.api.jobs.mappers.JobMapper;
import com.gabriel.empregos.core.exceptions.JobNotFoundException;
import com.gabriel.empregos.core.repositories.JobRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/{id}")
    public JobResponse findById(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow( () -> new JobNotFoundException(id) );


        return jobMapper.toJobResponse(job);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public JobResponse create(@RequestBody @Valid JobRequest jobRequest) {
        var job = jobMapper.toJob(jobRequest);
        job = jobRepository.save(job);
        return jobMapper.toJobResponse(job);
    }
}
