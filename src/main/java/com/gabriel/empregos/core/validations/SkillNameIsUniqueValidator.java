package com.gabriel.empregos.core.validations;

import org.springframework.stereotype.Component;

import com.gabriel.empregos.core.repositories.SkillRepository;
import com.gabriel.empregos.core.services.http.HttpService;

import jakarta.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SkillNameIsUniqueValidator implements ConstraintValidator<SkillNameIsUnique, String> {

    private final SkillRepository SkillRepository;
    private final HttpService httpService;

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        var id = httpService.getPathVariable("id", Long.class);

        if(id.isEmpty()){
            return !SkillRepository.existsByName(value);
        }

        return SkillRepository.existsByNameAndIdNot(value, id.get());
    }

}
