package com.flm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/hi")
	public String hi() {
		return "Hii!!!";
	}
	
	@GetMapping("/bye")
	public String bye() {
		return "bye";
	}
	
	@GetMapping("/open")
	public String open() {
		return "Open For All";
	}
	
	@GetMapping("/noAccess")
	public String noAccess() {
		return "No Access";
	}

}
