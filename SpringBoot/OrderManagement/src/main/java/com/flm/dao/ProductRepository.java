package com.flm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flm.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByProductNameContaining(String productName);
	
	@Query("update Product p set p.stock=:stock where p.productId=:productId")
	@Modifying
	@Transactional
	int updateStock(@Param(value = "productId") long productId,@Param(value = "stock") int stock);
}
