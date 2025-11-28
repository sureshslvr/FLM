package com.flm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flm.model.Person;

@RestController
public class LoginController {
	
	@GetMapping("/user/login")
	public String login() {
		return "You are logged in";
	}
	
	@GetMapping("/hi")
	public String hi() {
		return "Hii";
	}

}
