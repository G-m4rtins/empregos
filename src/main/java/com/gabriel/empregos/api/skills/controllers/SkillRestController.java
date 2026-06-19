package com.gabriel.empregos.api.skills.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.skills.dtos.SkillRequest;
import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.api.skills.mappers.SkillMapper;
import com.gabriel.empregos.core.exceptions.SkillNotFoundException;
import com.gabriel.empregos.core.repositories.SkillRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillRestController {

    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;

    @GetMapping
    public List<SkillResponse> findAll() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> skillMapper.toSkillResponse(skill))
                .toList();
        
    }

    @GetMapping("/{id}")
    public SkillResponse findById(@PathVariable Long id) {
        return skillRepository.findById(id).map(skill -> skillMapper.toSkillResponse(skill))
                .orElseThrow(SkillNotFoundException::new);

    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public SkillResponse create(@Valid @RequestBody SkillRequest skillRequest) {
        var skill = skillMapper.toSkill(skillRequest);
        var savedSkill = skillRepository.save(skill);
        return skillMapper.toSkillResponse(savedSkill);
    }

    @PutMapping("/{id}")
    public SkillResponse update(@PathVariable Long id, @Valid @RequestBody SkillRequest skillRequest) {
        var existingSkill = skillRepository
                            .findById(id)
                            .orElseThrow(SkillNotFoundException::new);
        
        BeanUtils.copyProperties(skillRequest, existingSkill, "id");
        var updatedSkill = skillRepository.save(existingSkill);
        return skillMapper.toSkillResponse(updatedSkill);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        var existingSkill = skillRepository
                            .findById(id)
                            .orElseThrow(SkillNotFoundException::new);
        
        skillRepository.delete(existingSkill);
    }

}
