package com.gabriel.empregos.api.skills.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;

import com.gabriel.empregos.api.skills.assemblers.SkillAssembler;
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
    private final SkillAssembler skillassembler;

    @GetMapping
    public CollectionModel<EntityModel<SkillResponse>> findAll() {
        var skills = skillRepository.findAll()
                .stream()
                .map(skillMapper::toSkillResponse)
                .toList();

        return skillassembler.toCollectionModel(skills);
    }

    @GetMapping("/{id}")
    public EntityModel<SkillResponse> findById(@PathVariable Long id) {
        var skill = skillRepository.findById(id).map(skillMapper::toSkillResponse)
                .orElseThrow(SkillNotFoundException::new);

        return skillassembler.toModel(skill);

    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public EntityModel<SkillResponse> create(@Valid @RequestBody SkillRequest skillRequest) {
        var skill = skillMapper.toSkill(skillRequest);
        var savedSkill = skillRepository.save(skill);
        var skillResponse = skillMapper.toSkillResponse(savedSkill);

        return skillassembler.toModel(skillResponse);
    }

    @PutMapping("/{id}")
    public EntityModel<SkillResponse> update(@PathVariable Long id, @Valid @RequestBody SkillRequest skillRequest) {
        var existingSkill = skillRepository
                            .findById(id)
                            .orElseThrow(SkillNotFoundException::new);

        BeanUtils.copyProperties(skillRequest, existingSkill, "id");
        var updatedSkill = skillRepository.save(existingSkill);
        var skillResponse = skillMapper.toSkillResponse(updatedSkill);

        return skillassembler.toModel(skillResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var existingSkill = skillRepository
                            .findById(id)
                            .orElseThrow(SkillNotFoundException::new);

        skillRepository.delete(existingSkill);
        return ResponseEntity.noContent().build();

    }

}
