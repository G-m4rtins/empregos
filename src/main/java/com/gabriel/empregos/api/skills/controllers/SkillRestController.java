package com.gabriel.empregos.api.skills.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.skills.dtos.SkillResponse;
import com.gabriel.empregos.api.skills.mappers.SkillMapper;
import com.gabriel.empregos.core.exceptions.SkillNotFoundException;
import com.gabriel.empregos.core.repositories.SkillRepository;

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

}
