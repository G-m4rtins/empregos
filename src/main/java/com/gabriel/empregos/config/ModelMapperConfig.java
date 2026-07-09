package com.gabriel.empregos.config;

import java.util.List;

import org.modelmapper.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import com.gabriel.empregos.api.jobs.dtos.JobRequest;
import com.gabriel.empregos.api.jobs.dtos.JobResponse;
import com.gabriel.empregos.core.models.Job;
import com.gabriel.empregos.core.models.Skills;
import com.gabriel.empregos.core.repositories.SkillRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final SkillRepository skillRepository;

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new org.modelmapper.ModelMapper();

        modelMapper.createTypeMap(JobRequest.class, Job.class)
                .addMappings(mapper -> mapper
                        .using(toListOfSkills())
                        .map(JobRequest::getSkills, Job::setSkills));
        modelMapper.createTypeMap(Job.class, JobResponse.class)
                .addMappings(mapper -> mapper
                        .map(src -> src.getCompany().getName(), JobResponse::setCompany));

        return modelMapper;
    }

    private Converter<List<Long>, List<Skills>> toListOfSkills() {
        return context -> skillRepository.findAllById(context.getSource());
    }

}
