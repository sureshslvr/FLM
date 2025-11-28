package com.flm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
	
	private long orderId;
	
	private String status;
	
	private double totalAmount;
	
	private List<OrderItemResponseDTO> orderItems;
}
