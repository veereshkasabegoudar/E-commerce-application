package com.veeresh.shopping.service;

import java.io.IOException;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.veeresh.shopping.dto.Customer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface CustomerService {
	String save(Customer customer,BindingResult result,HttpServletResponse response);

	String sendOtp(int id, ModelMap map);

	String resendOtp(int id, ModelMap map);
	
	String verifyOtp(int id, int otp, ModelMap map, HttpSession session,HttpServletResponse response);

	String login(String email, String password, ModelMap map, HttpSession session,HttpServletResponse response) ;

	String viewProducts(HttpSession session, ModelMap map,HttpServletResponse response);

	String addToCart(int id, HttpSession session,HttpServletResponse response);

	String viewCart(ModelMap map, HttpSession session,HttpServletResponse response);

	String removeFromCart(int id, HttpSession session,HttpServletResponse response);

	String paymentPage(HttpSession session, ModelMap map,HttpServletResponse response);

	String viewOrders(HttpSession session, ModelMap map,HttpServletResponse response);

	String confirmOrder(HttpSession session, int id, String razorpay_payment_id,HttpServletResponse response);

	
}
