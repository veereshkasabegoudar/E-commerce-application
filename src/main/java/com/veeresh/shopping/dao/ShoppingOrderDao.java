package com.veeresh.shopping.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.veeresh.shopping.dto.ShoppingOrder;
import com.veeresh.shopping.repository.ShoppingOrderRepository;
@Repository
public class ShoppingOrderDao {
	@Autowired
	ShoppingOrderRepository orderRepository;
	public void saveOrder(ShoppingOrder order) {
		orderRepository.save(order);
	}
	public ShoppingOrder findOrderById(int id) {
		return orderRepository.findById(id).orElseThrow();
	}
}
