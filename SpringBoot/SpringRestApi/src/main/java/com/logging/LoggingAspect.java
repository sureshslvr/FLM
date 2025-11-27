package com.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	/*
	 * @Before("execution(* com.service.*.*(..))") public void
	 * beforeAdvice(JoinPoint jp) { log.info("before method call ,{}"+
	 * jp.getSignature().getName()); }
	 */
	
	@Before("execution(* com.controller.*.*(..))")
	public void beforeAdviceController(JoinPoint jp) {
		log.info("before method call ,{}"+ jp.getSignature().getName());		
	}
	
	@Around("execution(* com.service.*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		log.info("before method Execution "+pjp.getSignature().getName());
		
		Object proceed = pjp.proceed();
		log.info("after method call "+proceed);
		
		log.info("after method execution "+ pjp.getSignature().getName());
		return proceed;
		
	}
	
	
	

}
