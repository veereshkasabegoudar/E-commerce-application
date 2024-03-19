package com.veeresh.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veeresh.shopping.dto.Customer;
import com.veeresh.shopping.dto.ProductInfo;

import jakarta.validation.Valid;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

	Customer findByEmail(String email);


	

}
