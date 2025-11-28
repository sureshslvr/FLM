package com.flm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flm.dao.OrderItemRepository;
import com.flm.dao.OrderRepository;
import com.flm.dao.ProductRepository;
import com.flm.dto.OrderItemResponseDTO;
import com.flm.dto.OrderRequestDTO;
import com.flm.dto.OrderResponseDTO;
import com.flm.exceptions.OrderItemNotFoundException;
import com.flm.exceptions.OrderNotFoundException;
import com.flm.model.Order;
import com.flm.model.OrderItem;
import com.flm.model.Product;
import com.flm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;

	@Override
	public OrderResponseDTO placeOrder(List<OrderRequestDTO> orderRequestDTOList) {
		Order order = new Order();
		List<OrderItem> orderItemsList = new ArrayList<>();
		order.setStatus("Ordered");
		
		for(OrderRequestDTO orderRequestDTO : orderRequestDTOList) {
			OrderItem orderItem = new OrderItem();
			
			Product product = productRepository.findById(orderRequestDTO.getProductId()).get();
			if(product.getStock()>= orderRequestDTO.getQuantity()) {
				orderItem.setQuantity(orderRequestDTO.getQuantity());
				orderItem.setProduct(product);
				orderItem.setOrder(order);
				orderItemsList.add(orderItem);
				productRepository.updateStock(product.getProductId(), product.getStock()- orderRequestDTO.getQuantity());
			}
		}
		order.setOrderItems(orderItemsList);
		
		Order savedOrder = orderRepository.save(order);
		
		
		return buildOrderResponseDtoFromOrder(savedOrder);
	}
	
	@Override
	public ResponseEntity<OrderResponseDTO> getOrderInfo(long orderId) {
		Order order = orderRepository.findById(orderId)
					   .orElseThrow(() -> new OrderNotFoundException("No Order Found with Id : "+orderId));
		OrderResponseDTO orderResponseDto = buildOrderResponseDtoFromOrder(order);
		return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
	}
	
	@Override
	public ResponseEntity<Void> cancelItem(long orderItemId) {
		OrderItem orderItem = orderItemRepository.findById(orderItemId)
							.orElseThrow(() -> new OrderItemNotFoundException("No Order Item Found with Id : "+orderItemId));
		orderItemRepository.delete(orderItem);
		
		long productId = orderItem.getProduct().getProductId();
		int stock = orderItem.getProduct().getStock();
		productRepository.updateStock(productId, stock+orderItem.getQuantity());
		return ResponseEntity.noContent().build();
	}


	private OrderResponseDTO buildOrderResponseDtoFromOrder(Order savedOrder) {
		OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
		
		orderResponseDTO.setOrderId(savedOrder.getOrderId());
		orderResponseDTO.setStatus(savedOrder.getStatus());
		
		List<OrderItemResponseDTO> orderItemResponseDTOList = new ArrayList<OrderItemResponseDTO>();
		double totalOrderAmount=0;
		for(OrderItem orderItem : savedOrder.getOrderItems()) {
			OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
			orderItemResponseDTO.setProductId(orderItem.getProduct().getProductId());
			orderItemResponseDTO.setProductName(orderItem.getProduct().getProductName());
			orderItemResponseDTO.setQuantity(orderItem.getQuantity());
			double eachProductPrice = orderItem.getProduct().getPrice() * ((100-orderItem.getProduct().getDiscount()) / 100);
			orderItemResponseDTO.setEachProductPrice(eachProductPrice);
			double totalProductPrice = eachProductPrice * orderItem.getQuantity();
			orderItemResponseDTO.setTotalProductPrice(totalProductPrice);
			totalOrderAmount += totalProductPrice;
			orderItemResponseDTOList.add(orderItemResponseDTO);
		}
		orderResponseDTO.setTotalAmount(totalOrderAmount);
		orderResponseDTO.setOrderItems(orderItemResponseDTOList);
		return orderResponseDTO;
	}

}
