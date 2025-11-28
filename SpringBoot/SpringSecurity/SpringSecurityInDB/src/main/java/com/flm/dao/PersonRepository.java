package com.flm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Person findByUsername(String username);
}
