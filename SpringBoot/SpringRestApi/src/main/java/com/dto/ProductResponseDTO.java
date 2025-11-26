package com.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private long productId;
    private String productName;
    private int discount;
    private boolean is_available;
    private double price;
    private double rating;
    private long stock;
}
