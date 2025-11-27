package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class SpringRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApiApplication.class, args);
	}

    @Bean
    TimedAspect timedAspect(MeterRegistry mr) {
		return new TimedAspect(mr);
	}
	
	@Bean
	CountedAspect countedAspect(MeterRegistry mr) {
		return new CountedAspect(mr);
	}
}
