package com.gabriel.empregos.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.empregos.core.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
