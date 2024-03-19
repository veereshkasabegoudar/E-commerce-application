package com.veeresh.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veeresh.shopping.dto.ProductInfo;

public interface productrepository extends JpaRepository<ProductInfo, Integer>{

	boolean existsByProductName(String productName);

	

	ProductInfo findByProductName(String productName);


}
