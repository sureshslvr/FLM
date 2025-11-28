package com.flm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.ProductRequestDto;
import com.flm.dto.ProductResponseDto;
import com.flm.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/save")
	public ProductResponseDto saveProduct(@RequestBody ProductRequestDto productRequestDto) {
		return productService.save(productRequestDto);
	}
	
	@PostMapping("/save/all")
	public List<ProductResponseDto> saveAllProducts(@RequestBody List<ProductRequestDto> productRequestDtos){
		return productService.saveAllProducts(productRequestDtos);
	}
	
	@GetMapping
	public List<ProductResponseDto> getProducts(){
		return productService.getProducts();
	}
	
	@GetMapping("/{id}")
	public ProductResponseDto getProductById(@PathVariable(name = "id") long id) {
		return productService.getProduct(id);
	}
	
	@GetMapping("/name")
	public List<ProductResponseDto> getProductByName(@RequestParam(name = "productName") String productName) {
		return productService.getProductByName(productName);
	}
	
	@PutMapping("/update/{id}")
	public ProductResponseDto updateProductRating(@PathVariable(name = "id") long id, @RequestParam(name = "rating") double rating) {
		return productService.updateProductByRating(id,rating);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") long id) {
		return productService.deleteProduct(id);
	}
	
	
}
