package com.controller;

import java.util.List;

import com.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ProductRequestDTO;
import com.model.Products;
import com.service.ProductService;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Timed(value = "order.get.time")
	@Counted(value = "order.get.count")
	@GetMapping("/getAll")
	public List<ProductResponseDTO> getAllProducts(){
		return productService.getAllProducts();

	}

    @GetMapping("/getSimilarProductByName/{name}")
    public List<ProductResponseDTO> getSimilarProducts(@PathVariable String name){
        return productService.getSimilarProducts(name);

    }

    @GetMapping("/getProductByName")
    public ResponseEntity<ProductResponseDTO> getProductByName(@RequestParam String name){
        return productService.getProductByName(name);
    }
    @DeleteMapping("/deleteProduct/{id}")
    public void removeProduct(@PathVariable Long id){
        productService.removeProduct(id);
    }
	
	@PostMapping("/save")
	public String saveProduct(@RequestBody ProductRequestDTO dto){
		return productService.saveProduct(dto);
		
	}

}
