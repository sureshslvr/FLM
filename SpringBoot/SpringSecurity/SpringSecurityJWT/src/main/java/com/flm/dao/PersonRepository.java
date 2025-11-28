package com.flm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flm.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByEmail(String email);
}
