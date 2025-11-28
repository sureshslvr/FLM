package com.flm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/hi")
	public String hi() {
		return "Hi you have valid JWT";
	}
	
	@GetMapping("/open")
	public String open() {
		return "You can access without JWT";
	}

}
