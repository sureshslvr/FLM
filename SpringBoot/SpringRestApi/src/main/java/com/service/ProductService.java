package com.service;

import java.util.List;

import com.dto.ProductRequestDTO;
import com.dto.ProductResponseDTO;
import com.model.Products;

public interface ProductService {
	
	List<ProductResponseDTO> getAllProducts();
	
	String saveProduct(ProductRequestDTO dto);

    List<ProductResponseDTO> getSimilarProducts(String name);
}
