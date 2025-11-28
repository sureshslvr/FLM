package com.flm.exceptions;

public class OrderItemNotFoundException extends RuntimeException{
	
	public OrderItemNotFoundException(String message) {
		super(message);
	}
}
