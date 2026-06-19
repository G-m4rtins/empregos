package com.gabriel.empregos.api.skills.mappers;

import com.gabriel.empregos.api.skills.dtos.SkillRequest;
import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.core.models.skills;

public interface SkillMapper {

    skills toSkill(SkillRequest request);
    SkillResponse toSkillResponse(skills skill);
    

}
