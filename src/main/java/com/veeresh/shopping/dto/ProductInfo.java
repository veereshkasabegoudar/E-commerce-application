package com.veeresh.shopping.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Component
@Data
@Entity
public class ProductInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Size(min = 5, max = 50, message = "*should be between 5 to 50 characters ")
	private String productName;

	@DecimalMin(value = "1", message = "*Enter atleast minimum price: 1 Rs")
	private double ProductPrice;

	@Size(min = 5, max = 20, message = "*should be between 5 to 20 characters ")
	private String ProductCategory;

	@DecimalMin(value = "1", message = "*Enter atleast minimum price: 1 Rs")
	double ProductStock;

	@Size(min = 5, max = 100, message = "*should be between 5 to 100 characters ")
	private String productDescription;

	private String imagePath;

}
