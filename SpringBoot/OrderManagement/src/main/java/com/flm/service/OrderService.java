package com.flm.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.flm.dto.OrderRequestDTO;
import com.flm.dto.OrderResponseDTO;

public interface OrderService {
	
	public OrderResponseDTO placeOrder(List<OrderRequestDTO> orderRequestDTOList);

	public ResponseEntity<OrderResponseDTO> getOrderInfo(long orderId);

	public ResponseEntity<Void> cancelItem(long orderItemId);
}
