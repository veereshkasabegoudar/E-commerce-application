package com.veeresh.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.veeresh.shopping.dto.Customer;
import com.veeresh.shopping.dto.ProductInfo;
import com.veeresh.shopping.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AdminService adminService;

	@GetMapping()
	public String loadDashboard(HttpSession session) {
		return adminService.loadDashboard(session);
	}
	@GetMapping("/add-product")
	public String Addproducts(HttpSession session,ModelMap map) {
		return adminService.Addproduct(session,map);
	}
	@PostMapping("/add-product")
	public String Signin(@Valid ProductInfo productInfo, BindingResult result,ModelMap map,HttpSession session,@RequestParam MultipartFile  picture ) {
		System.out.println(result.getAllErrors());
		if (result.hasErrors()) 
			return "AddProduct";
		else {
			return adminService.addproduct(productInfo, result,map,session,picture);
		}

	}
	@GetMapping("/manage-products")
	public String manageProducts(HttpSession session, ModelMap map) {
		return adminService.manageproducts(session, map);
	}
	@GetMapping("/delete/{id}")
	public String deleteproduct(@PathVariable int id,HttpSession session) {
		return adminService.deleteproduct(id,session);
	}
	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable int id,HttpSession session,ModelMap map) {
	
		return adminService.editProduct(id,session,map);
	}
	@PostMapping("/update-product")
	public String updateProdcut(@Valid ProductInfo productInfo, BindingResult result, @RequestParam MultipartFile picture,
			HttpSession session, ModelMap map) {
	
		return adminService.updateProduct(productInfo, result, picture, session, map);
	}
	@GetMapping("/create-admin/{email}/{password}")
	public String createAdmin(@PathVariable String email, @PathVariable String password,HttpSession session) {
		return adminService.createAdmin(email,password,session);
	}
}

	
	


