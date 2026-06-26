package com.gabriel.empregos.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gabriel.empregos.core.models.Skills;

public interface SkillRepository extends JpaRepository<Skills, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String value, Long id);

}
