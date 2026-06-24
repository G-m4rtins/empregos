package com.gabriel.empregos.api.skills.dtos;

import com.gabriel.empregos.core.validations.SkillNameIsUnique;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {

    @NotEmpty(message = "O nome da habilidade é obrigatório")
    @SkillNameIsUnique
    private String name;

}
