package com.flm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.flm.dao.ProductRepository;
import com.flm.dto.ProductRequestDto;
import com.flm.dto.ProductResponseDto;
import com.flm.model.Product;
import com.flm.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public ProductResponseDto save(ProductRequestDto productRequestDto) {
		Product product = new Product();
		product.setProductName(productRequestDto.getProductName());
		product.setPrice(productRequestDto.getPrice());
		product.setDiscount(productRequestDto.getDiscount());
		product.setStock(productRequestDto.getStock());
		if(product.getStock()>0) {
			product.setAvailable(true);
		}
		
		Product savedproduct = productRepository.save(product);
		
		ProductResponseDto productResponseDto = new ProductResponseDto();
		BeanUtils.copyProperties(savedproduct, productResponseDto);
		return productResponseDto;
	}

	@Override
	public List<ProductResponseDto> getProducts() {
		List<Product> products = productRepository.findAll();
		
		List<ProductResponseDto> productsList = buildProductsResponseList(products);
		return productsList;
	}

	@Override
	public ProductResponseDto getProduct(long id) {
		Product product = productRepository.findById(id).get();
		
		ProductResponseDto productResponseDto = new ProductResponseDto();
		BeanUtils.copyProperties(product, productResponseDto);
		return productResponseDto;
	}

	@Override
	public List<ProductResponseDto> getProductByName(String productName) {
		List<Product> products = productRepository.findByProductNameContaining(productName);
		List<ProductResponseDto> productsResponseList = buildProductsResponseList(products);
		return productsResponseList;
	}
	
	@Override
	public List<ProductResponseDto> saveAllProducts(List<ProductRequestDto> productRequestDtos) {
		
		List<Product> products = buildProductsList(productRequestDtos);
		
		List<Product> savedProducts = productRepository.saveAll(products);
		List<ProductResponseDto> productsResponseList = buildProductsResponseList(savedProducts);
		return productsResponseList;
	}
	
	@Override
	public ProductResponseDto updateProductByRating(long id, double rating) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if(optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			product.setRating(rating);
			
			Product savedProduct = productRepository.save(product);
			ProductResponseDto productResponseDto = new ProductResponseDto();
			BeanUtils.copyProperties(savedProduct, productResponseDto);
			return productResponseDto;
		}
		return new ProductResponseDto();
	}
	
	@Override
	public String deleteProduct(long id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if(optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			productRepository.delete(product);
			return product.getProductName();
		}
		return null;
	}

	private List<Product> buildProductsList(List<ProductRequestDto> productRequestDtos) {
		List<Product> products = new ArrayList<>();
		for(ProductRequestDto productRequestDto : productRequestDtos) {
			Product product = new Product();
			BeanUtils.copyProperties(productRequestDto, product);
			product.setAvailable(true);
			products.add(product);
		}
		return products;
	}

	
	private List<ProductResponseDto> buildProductsResponseList(List<Product> products) {
		List<ProductResponseDto> productsList = new ArrayList<>();
		
		for(Product product : products) {
			ProductResponseDto productResponseDto = new ProductResponseDto();
			BeanUtils.copyProperties(product, productResponseDto);
			productsList.add(productResponseDto);
		}
		return productsList;
	}

}
