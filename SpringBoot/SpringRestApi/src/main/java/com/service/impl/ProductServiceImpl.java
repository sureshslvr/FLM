package com.service.impl;

import java.util.ArrayList;
import java.util.List;


import com.dto.ProductResponseDTO;
import com.exception.ProductNotFoundException;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dto.ProductRequestDTO;
import com.model.Products;
import com.repository.ProductRepository;
import com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{


	@Autowired
	ProductRepository productRepository;

    private static final Logger LOGGER=LoggerFactory.getLogger(ProductServiceImpl.class);
	@Override
	public List<ProductResponseDTO> getAllProducts() {
        List<Products> all = productRepository.findAll();
        return getProductResponseDTOList(all);
	}

    private static List<ProductResponseDTO> getProductResponseDTOList(List<Products> all) {
        List<ProductResponseDTO> productResponseDTOList=new ArrayList<>();
        for(Products product: all){
            ProductResponseDTO productResponseDTO=new ProductResponseDTO();
            productResponseDTO.setProductId(product.getProductId());
            productResponseDTO.setProductName(product.getProductName());
            productResponseDTO.setDiscount(product.getDiscount());
            productResponseDTO.set_available(product.is_available());
            productResponseDTO.setPrice(product.getPrice());
            productResponseDTO.setRating(product.getRating());
            productResponseDTO.setStock(product.getStock());
            productResponseDTOList.add(productResponseDTO);
        }
        return productResponseDTOList;
    }


    @Override
	public String saveProduct(ProductRequestDTO dto) {
		Products p=new Products();
		p.setProductName(dto.getProduct_name());
		p.setDiscount(dto.getDiscount());
		if(dto.getStock()>=1) {
			p.set_available(true);
		}
		p.setPrice(dto.getPrice());
		p.setStock(dto.getStock());
		
		return productRepository.save(p).getProductName();
	}

    @Override
    public List<ProductResponseDTO> getSimilarProducts(String name) {
        List<Products> productContainsName = productRepository.findByProductNameContaining(name);

        return getProductResponseDTOList(productContainsName);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> getProductByName(String name) {
    	LOGGER.warn("getProductByName method start : "+name);
        Products product = productRepository.findByProductName(name)
                .orElseThrow(()->new ProductNotFoundException("product not found with name "+name));
        ProductResponseDTO productResponseDTO=new ProductResponseDTO();
        BeanUtils.copyProperties(product,productResponseDTO);
        LOGGER.warn("getProductByName method end : "+productResponseDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productResponseDTO);
    }

    @Override
    public void removeProduct(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("product not found with id: "+id));
        productRepository.delete(product);
    }


}
