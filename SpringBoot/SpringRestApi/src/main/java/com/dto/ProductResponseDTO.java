package com.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private long product_id;
    private String product_name;
    private int discount;
    private boolean is_available;
    private double price;
    private double rating;
    private long stock;
}
