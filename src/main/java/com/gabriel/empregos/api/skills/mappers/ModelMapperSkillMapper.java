package com.gabriel.empregos.api.skills.mappers;

import org.springframework.stereotype.Component;

import com.gabriel.empregos.api.skills.dtos.SkillRequest;
import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.core.models.skills;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapperSkillMapper implements SkillMapper {

    private final org.modelmapper.ModelMapper modelMapper;

    @Override
    public skills toSkill(SkillRequest request) {
        return modelMapper.map(request, skills.class);
    }

    @Override
    public SkillResponse toSkillResponse(skills skill) {
        return modelMapper.map(skill, SkillResponse.class);
    }

 

}
