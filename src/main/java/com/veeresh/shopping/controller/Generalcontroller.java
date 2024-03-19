package com.veeresh.shopping.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.veeresh.shopping.dto.Customer;
import com.veeresh.shopping.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class Generalcontroller {
	@Autowired
	Customer customer;
	@Autowired
	CustomerService customerService;

	@GetMapping("/")
	public String loadHome() {
		return "Home";
	}

	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		map.put("customer", customer);
		return "Signup";
	}

	@GetMapping("/signin")
	public String LoadSignin() {
		return "Login";
	}

	@PostMapping("/signup")
	public String Signin(@Valid Customer customer, BindingResult result,HttpServletResponse response) {
		if (result.hasErrors()) {
			return "signup";
		} else {
			return customerService.save(customer, result,response);
		}

	}
	@GetMapping("/send-otp/{id}")
	public String sendOtp(@PathVariable int id,ModelMap map)
	{
		return customerService.sendOtp(id,map);
	}
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int id,@RequestParam int otp,ModelMap map,HttpSession session,HttpServletResponse response) {
		return customerService.verifyOtp(id,otp,map,session,response);
	}
	
	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, ModelMap map) {
		return customerService.resendOtp(id,map);
	}
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, ModelMap map, HttpSession session,HttpServletResponse response) throws IOException {
		return customerService.login(email, password, map, session,response);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("customer");
		session.setAttribute("successMessage", "Logout Success");
		return "redirect:/";
	}
	@GetMapping("/products")
	public String viewProducts(ModelMap map, HttpSession session,HttpServletResponse response) {
		return customerService.viewProducts(session, map,response);
	}
	@GetMapping("/add-cart/{id}")
	public String addToCart(@PathVariable int id,HttpSession session,HttpServletResponse response) {
		return customerService.addToCart(id, session,response);
	}
	@GetMapping("/cart")
	public String viewCart(HttpSession session,ModelMap map,HttpServletResponse response) {
		return customerService.viewCart(map, session,response);
	}
	@GetMapping("/remove-cart/{id}")
	public String removeFromCart(@PathVariable int id,HttpSession session,HttpServletResponse response) {
		return customerService.removeFromCart(id, session,response);
	}
	@GetMapping("/payment")
	public String paymentPage(HttpSession session,ModelMap map,HttpServletResponse response) {
		return customerService.paymentPage(session,map,response);

	}
	@PostMapping("/confirm-order/{id}")
	public String confirmOrder(HttpSession session,@PathVariable int id,@RequestParam String razorpay_payment_id,HttpServletResponse response) {
		return customerService.confirmOrder(session,id,razorpay_payment_id,response);
	}
	@GetMapping("/orders")
	public String viewOrders(HttpSession session,ModelMap map,HttpServletResponse response) {
		return customerService.viewOrders(session,map,response);
	}
}
