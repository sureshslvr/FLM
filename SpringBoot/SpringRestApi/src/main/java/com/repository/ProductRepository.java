package com.repository;

import com.dto.ProductResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Products;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>{
	
    public List<Products> findByProductNameContaining(String name);
}
