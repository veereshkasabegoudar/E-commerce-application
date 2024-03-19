package com.veeresh.shopping.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.veeresh.shopping.dto.ProductInfo;
import com.veeresh.shopping.repository.productrepository;

import jakarta.validation.Valid;
@Repository
public class Productinfodao {
	@Autowired
	productrepository productrepository;
	
	public void save(ProductInfo productInfo) {
		productrepository.save(productInfo);
		
	}

	public boolean chechname(String productName) {
		
		return productrepository.existsByProductName(productName);
	}

	public List<ProductInfo> fetchAll() {
		
		return productrepository.findAll();
	}

	public ProductInfo findById(int id) {
	
			return productrepository.findById(id).orElseThrow();
		}

		public void delete(ProductInfo product) {
			productrepository.delete(product);
		}

		public ProductInfo findByName(String productName) {
			return productrepository.findByProductName(productName);
		}
	}






