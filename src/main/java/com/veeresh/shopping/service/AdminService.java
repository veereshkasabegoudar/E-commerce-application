package com.veeresh.shopping.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.veeresh.shopping.dto.ProductInfo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface AdminService {

	String loadDashboard(HttpSession session);

	String Addproduct(HttpSession session, ModelMap map);

	String addproduct( ProductInfo productInfo, BindingResult result, ModelMap map,HttpSession session, MultipartFile picture);

	String manageproducts(HttpSession session, ModelMap map);

	String deleteproduct(int id,HttpSession session);

	String editProduct(int id, HttpSession session, ModelMap map);

	String updateProduct(@Valid ProductInfo product, BindingResult result, MultipartFile picture, HttpSession session,
			ModelMap map);

	String createAdmin(String email, String password, HttpSession session);




}
