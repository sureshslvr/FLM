package com.flm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.OrderRequestDTO;
import com.flm.dto.OrderResponseDTO;
import com.flm.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/buy")
	public OrderResponseDTO placeOrder(@RequestBody List<OrderRequestDTO> orderRequestDTOs) {
		return orderService.placeOrder(orderRequestDTOs);
	}
	
	@DeleteMapping("/cancel")
	public ResponseEntity<Void> cancelItem(@RequestParam(name = "orderItemId") long orderItemId) {
		return orderService.cancelItem(orderItemId);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDTO> getOrderInfo(@PathVariable(name = "orderId") long orderId) {
		return orderService.getOrderInfo(orderId);
	}
	
	

}
