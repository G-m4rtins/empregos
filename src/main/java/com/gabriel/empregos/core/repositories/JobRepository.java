package com.gabriel.empregos.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.empregos.core.models.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByCompanyEmailAndId(String companyEmail, Long id);

}
