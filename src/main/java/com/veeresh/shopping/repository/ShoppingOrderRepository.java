package com.veeresh.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veeresh.shopping.dto.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Integer> {

	

}
