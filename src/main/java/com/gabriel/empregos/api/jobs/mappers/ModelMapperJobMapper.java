package com.gabriel.empregos.api.jobs.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gabriel.empregos.api.jobs.dtos.JobRequest;
import com.gabriel.empregos.api.jobs.dtos.JobResponse;
import com.gabriel.empregos.core.models.Job;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapperJobMapper implements JobMapper{

    private final ModelMapper modelMapper;

    @Override
    public JobResponse toJobResponse(Job job) {
        return modelMapper.map(job, JobResponse.class);
    }

    @Override
    public Job toJob(JobRequest jobRequest) {
        return modelMapper.map(jobRequest, Job.class);
    }

}

