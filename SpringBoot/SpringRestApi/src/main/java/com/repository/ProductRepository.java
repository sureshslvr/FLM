package com.repository;

import com.dto.ProductResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Products;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>{
	
    List<Products> findByProductNameContaining(String name);


    Optional<Products> findByProductName(String name);
}
