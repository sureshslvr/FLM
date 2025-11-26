package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Products {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long product_id;
    @Column(name = "product_name")
    private String productName;
	private int discount;
	private boolean is_available;
	private double price;
	private double rating;
	private long stock;
	
	public Products(int discount, boolean is_available, double price, String product_name, double rating, long stock) {
		super();
		this.discount = discount;
		this.is_available = is_available;
		this.price = price;
		this.productName = product_name;
		this.rating = rating;
		this.stock = stock;
	}
	
	
}
