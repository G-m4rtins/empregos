package com.gabriel.empregos.config;

import java.util.List;

import org.modelmapper.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gabriel.empregos.api.jobs.dtos.JobRequest;
import com.gabriel.empregos.core.models.Job;
import com.gabriel.empregos.core.models.Skills;
import com.gabriel.empregos.core.repositories.SkillRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final SkillRepository skillRepository;

    @Bean
    public org.modelmapper.ModelMapper modelMapper() {
        var modelMapper = new org.modelmapper.ModelMapper();

        modelMapper.createTypeMap(JobRequest.class, Job.class)
            .addMappings(mapper -> mapper
                .using(toListOfSkills())
                .map(JobRequest::getSkills, Job::setSkills)
        );
        return modelMapper;
    }

    private Converter<List<Long>, List<Skills>> toListOfSkills() {
        return context -> skillRepository.findAllById(context.getSource());
    }

}
