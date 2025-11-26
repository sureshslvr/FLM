package com.controller;

import java.util.List;

import com.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dto.ProductRequestDTO;
import com.model.Products;
import com.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/getAll")
	public List<ProductResponseDTO> getAllProducts(){
		return productService.getAllProducts();

	}

    @GetMapping("/getSimilarProductByName/{name}")
    public List<ProductResponseDTO> getSimilarProducts(@PathVariable String name){
        return productService.getSimilarProducts(name);

    }
	
	@PostMapping("/save")
	public String saveProduct(@RequestBody ProductRequestDTO dto){
		return productService.saveProduct(dto);
		
	}

}
