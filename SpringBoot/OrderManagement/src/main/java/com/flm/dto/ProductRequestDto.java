package com.flm.dto;

import lombok.Data;

@Data
public class ProductRequestDto {
	
	private String productName;
	
	private double price;
	
	private double discount;
	
	private int stock;
	
}
