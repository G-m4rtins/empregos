package com.gabriel.empregos.api.skills.mappers;

import org.springframework.stereotype.Component;

import com.gabriel.empregos.api.skills.dtos.SkillRequest;
import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.core.models.Skills;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapperSkillMapper implements SkillMapper {

    private final org.modelmapper.ModelMapper modelMapper;

    @Override
    public Skills toSkill(SkillRequest request) {
        return modelMapper.map(request, Skills.class);
    }

    @Override
    public SkillResponse toSkillResponse(Skills skill) {
        return modelMapper.map(skill, SkillResponse.class);
    }



}
