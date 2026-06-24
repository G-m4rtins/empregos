package com.gabriel.empregos.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gabriel.empregos.core.models.skills;

public interface SkillRepository extends JpaRepository<skills, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String value, Long id);

}
