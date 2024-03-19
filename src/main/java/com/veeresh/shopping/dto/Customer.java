package com.veeresh.shopping.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Component
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message = "* it is  complusary required field ")
	@Size(min = 3, max = 15, message = "*should be between 3 to 15 characters ")
	private String name;
	@NotEmpty(message = "* it is  complusary required field ")
	@Email(message = "* Enter the valid format")
	private String email;
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "* Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character")
	@NotEmpty(message = "* it is  complusary required field ")
	private String password;
	@NotEmpty(message = "* it is  complusary required field ")
	private String gender;

	@NotNull(message = "* it is  complusary required field ")
	@DecimalMax(value = "9999999999", inclusive = true, message = "* Enter Proper Mobile Number")
	@DecimalMin(value = "6000000000", inclusive = true, message = "* Enter Proper Mobile Number")
	private long mobile;
	@NotNull(message = "* it is  complusary required field ")
	@Past(message = "* enter the proper dob")
	private LocalDate dob;
	private String role;
	private int otp;
	private boolean verified;
	
	@OneToOne(cascade = CascadeType.ALL)
	Cart cart = new Cart();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<ShoppingOrder> orders = new ArrayList<ShoppingOrder>();
}
