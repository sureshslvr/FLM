package com.flm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
