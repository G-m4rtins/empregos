package com.gabriel.empregos.api.jobs.mappers;

import com.gabriel.empregos.api.jobs.dtos.JobRequest;
import com.gabriel.empregos.api.jobs.dtos.JobResponse;
import com.gabriel.empregos.core.models.Job;

public interface JobMapper {

    JobResponse toJobResponse(Job job);
    Job toJob(JobRequest jobRequest);

}
