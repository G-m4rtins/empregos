package com.gabriel.empregos.api.skills.mappers;

import com.gabriel.empregos.api.skills.dtos.SkillRequest;
import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.core.models.Skills;

public interface SkillMapper {

    Skills toSkill(SkillRequest request);
    SkillResponse toSkillResponse(Skills skill);


}
