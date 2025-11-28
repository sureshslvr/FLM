package com.flm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dao.PersonRepository;
import com.flm.model.Person;

@RestController
public class SignUpController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	PersonRepository personRepository;
	
	@PostMapping("/signup")
	public Person signup(@RequestBody Person person) {
		String encodedPassword = passwordEncoder.encode(person.getPassword());
		person.setPassword(encodedPassword);
		return personRepository.save(person);
	}
}
