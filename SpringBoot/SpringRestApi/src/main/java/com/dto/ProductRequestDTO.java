package com.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
	
	private String product_name;
	
	private int discount;
	
	private double price;
	
	private long stock;

}
