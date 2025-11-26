package com.service;

import java.util.List;

import com.dto.ProductRequestDTO;
import com.dto.ProductResponseDTO;
import com.model.Products;
import org.springframework.http.ResponseEntity;

public interface ProductService {
	
	List<ProductResponseDTO> getAllProducts();
	
	String saveProduct(ProductRequestDTO dto);

    List<ProductResponseDTO> getSimilarProducts(String name);

    ResponseEntity<ProductResponseDTO> getProductByName(String name);

    void removeProduct(Long id);

}
