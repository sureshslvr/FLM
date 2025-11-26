package com.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.ProductRequestDTO;
import com.model.Products;
import com.repository.ProductRepository;
import com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	@Override
	public List<ProductResponseDTO> getAllProducts() {
        List<Products> all = productRepository.findAll();
        return getProductResponseDTOList(all);
	}

    private static List<ProductResponseDTO> getProductResponseDTOList(List<Products> all) {
        List<ProductResponseDTO> productResponseDTOList=new ArrayList<>();
        for(Products product: all){
            ProductResponseDTO productResponseDTO=new ProductResponseDTO();
            productResponseDTO.setProduct_id(product.getProduct_id());
            productResponseDTO.setProduct_name(product.getProductName());
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


}
