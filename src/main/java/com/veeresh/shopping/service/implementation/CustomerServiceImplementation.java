package com.veeresh.shopping.service.implementation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.veeresh.shopping.dao.Customerdao;
import com.veeresh.shopping.dao.Itemdao;
import com.veeresh.shopping.dao.Productinfodao;
import com.veeresh.shopping.dao.ShoppingOrderDao;
import com.veeresh.shopping.dto.Cart;
import com.veeresh.shopping.dto.Customer;
import com.veeresh.shopping.dto.Item;
import com.veeresh.shopping.dto.ProductInfo;
import com.veeresh.shopping.dto.ShoppingOrder;
import com.veeresh.shopping.helper.AES;
import com.veeresh.shopping.helper.MailSendingHelper;
import com.veeresh.shopping.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class CustomerServiceImplementation implements CustomerService {
	@Autowired
	Customerdao customerdao;
	@Autowired
	Productinfodao productinfodao;
	@Autowired
	Itemdao itemdao;
	@Autowired
	ShoppingOrderDao orderDao;
	@Autowired
	MailSendingHelper mailHelper;

	@Override
	public String save(Customer customer, BindingResult result, HttpServletResponse response) {
		// to check the duplicate email and mobile number
		if (customerdao.checkEmailDuplicate(customer.getEmail()))
			result.rejectValue("email", "error.email", "* Account Already Exists with this Email");
		if (customerdao.checkMobileDuplicate(customer.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Account Already Exists with this Mobile");
		// to check errors
		if (result.hasErrors())
			return "Signup";
		else {
			customer.setPassword(AES.encrypt(customer.getPassword(), "123"));
			customer.setRole("USER");

			customerdao.save(customer);
			// when we refresh it must be in same page so used redirect
			// sending mail logic
			
				 return "redirect:/send-otp/" + customer.getId();
			
		}
	}

	@Override
	public String verifyOtp(int id, int otp, ModelMap map, HttpSession session, HttpServletResponse response) {
		Customer customer = customerdao.findById(id);
		if (customer.getOtp() == otp) {
			customer.setVerified(true);
			customerdao.save(customer);
			 mailHelper.resendOtp(customer);
			session.setAttribute("successMessage", "Account Created Success");
			
			return "redirect:/signin";
		} else {
			map.put("failMessage", "Invalid Otp, Try Again!");
			map.put("id", id);
			return "VerifyOtp";
		}
	}

	@Override
	public String sendOtp(int id, ModelMap map) {
		Customer customer = customerdao.findById(id);
		customer.setOtp(new Random().nextInt(100000, 999999));
		customerdao.save(customer);
		// Logic for Sending Otp
		 mailHelper.resendOtp(customer);
		map.put("id", id);
		map.put("successMessage", "Otp Sent Success to Email");
		return "VerifyOtp";
	}

	@Override
	public String resendOtp(int id, ModelMap map) {
		Customer customer = customerdao.findById(id);
		customer.setOtp(new Random().nextInt(100000, 999999));
		customerdao.save(customer);
		// Logic for Resending Otp
		 mailHelper.resendOtp(customer);
		map.put("id", id);
		map.put("successMessage", "Otp Resent Success");
		return "VerifyOtp";
	}

	@Override
	public String login(String email, String password, ModelMap map, HttpSession session,
			HttpServletResponse response) {
		Customer customer = customerdao.findByEmail(email);
		if (customer == null)
			session.setAttribute("failMessage", "Invalid Email");
		else {
			if (AES.decrypt(customer.getPassword(), "123").equals(password)) {
				if (customer.isVerified()) {
					session.setAttribute("customer", customer);
					session.setAttribute("successMessage", "Login Success");

					return	"redirect:/";
					
				} else {
					return resendOtp(customer.getId(), map);
				}
			} else
				session.setAttribute("failMessage", "Invalid Password");
		}
		return "Login";
	}

	@Override
	public String viewProducts(HttpSession session, ModelMap map, HttpServletResponse response) {
		List<ProductInfo> products = productinfodao.fetchAll();
		if (products.isEmpty()) {
			session.setAttribute("failMessage", "No Products Present");
			try {
				response.sendRedirect("/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/";
		} else {
			map.put("products", products);
			return "ViewProducts";
		}
	}

	@Override
	public String addToCart(int id, HttpSession session, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			
			return "redirect:/";
		} else {
			ProductInfo product = productinfodao.findById(id);
			if (product.getProductStock() > 0) {
				Cart cart = customer.getCart();
				List<Item> items = cart.getItems();
				if (items.isEmpty()) {
					Item item = new Item();
					item.setProductCategory(product.getProductCategory());
					item.setProductDescription(product.getProductDescription());
					item.setImagePath(product.getImagePath());
					item.setProductName(product.getProductName());
					item.setProductPrice(product.getProductPrice());
					item.setQuantity(1);
					items.add(item);
					session.setAttribute("successMessage", "Item added to Cart Success");
				} else {
					boolean flag = true;
					for (Item item : items) {
						if (item.getProductName().equals(product.getProductName())) {
							flag = false;
							if (item.getQuantity() < product.getProductStock()) {
								item.setQuantity(item.getQuantity() + 1);
								item.setProductPrice(item.getProductPrice() + product.getProductPrice());
								session.setAttribute("successMessage", "Item added to Cart Success");
							} else {
								session.setAttribute("failMessage", "Out of Stock");
							}
							break;
						}
					}
					if (flag) {
						Item item = new Item();
						item.setProductCategory(product.getProductCategory());
						item.setProductDescription(product.getProductDescription());
						item.setImagePath(product.getImagePath());
						item.setProductName(product.getProductName());
						item.setProductPrice(product.getProductPrice());
						item.setQuantity(1);
						items.add(item);
						session.setAttribute("successMessage", "Item added to Cart Success");
					}
				}
				customerdao.save(customer);
				session.setAttribute("customer", customerdao.findById(customer.getId()));
				
				return "redirect:/products";
			} else {
				session.setAttribute("failMessage", "Out of Stock");
				
				return "redirect:/";
			}
		}
	}

	@Override
	public String viewCart(ModelMap map, HttpSession session, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			
			return "redirect:/signin";
		} else {
			Cart cart = customer.getCart();
			List<Item> items = cart.getItems();
			if (items.isEmpty()) {
				session.setAttribute("failMessage", "No Items in cart");
				
				return "redirect:/";
			} else {
				map.put("items", items);
				return "ViewCart";
			}
		}
	}

	@Override
	public String removeFromCart(int id, HttpSession session, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			
			return "redirect:/signin";
		} else {
			Item item = itemdao.findById(id);
			if (item.getQuantity() == 1) {
				customer.getCart().getItems().remove(item);
				customerdao.save(customer);
				session.setAttribute("customer", customerdao.findById(customer.getId()));
				itemdao.delete(item);
				session.setAttribute("successMessage", "Item Removed from Cart");

			} else {
				item.setProductPrice(item.getProductPrice() - (item.getProductPrice() / item.getQuantity()));
				item.setQuantity(item.getQuantity() - 1);
				itemdao.save(item);
				session.setAttribute("successMessage", "Item Quantity Reduced By 1");
			}
			session.setAttribute("customer", customerdao.findById(customer.getId()));
		
			return "redirect:/cart";
		}
	}

	@Override
	public String paymentPage(HttpSession session, ModelMap map, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			
			return "redirect:/signin";
		} else {

			List<Item> items = customer.getCart().getItems();
			if (items.isEmpty()) {
				session.setAttribute("failMessage", "Nothing to Buy");
				
				return "redirect:/";
			} else {
				double price = items.stream().mapToDouble(x -> x.getProductPrice()).sum();
				try {
					RazorpayClient razorpay = new RazorpayClient("rzp_test_8TRRhHc80WlT5W", "tQhU6EzLAH3qvsLhrg08OiLU");

					JSONObject orderRequest = new JSONObject();
					orderRequest.put("amount", price * 100);
					orderRequest.put("currency", "INR");

					Order order = razorpay.orders.create(orderRequest);

					ShoppingOrder myOrder = new ShoppingOrder();
					myOrder.setDateTime(LocalDateTime.now());
					myOrder.setItems(items);
					myOrder.setOrderId(order.get("id"));
					myOrder.setStatus(order.get("status"));
					myOrder.setTotalPrice(price);

					orderDao.saveOrder(myOrder);

					map.put("key", "rzp_test_8TRRhHc80WlT5W");
					map.put("myOrder", myOrder);
					map.put("customer", customer);

					customer.getOrders().add(myOrder);
					customerdao.save(customer);
					session.setAttribute("customer", customerdao.findById(customer.getId()));
					return "PaymentPage";

				} catch (RazorpayException e) {
					e.printStackTrace();
					
					return "redirect:/"; 
				}
			}
		}
	}

	@Override
	public String confirmOrder(HttpSession session, int id, String razorpay_payment_id, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			
			return "redirect:/signin";
		} else {
			for (Item item : customer.getCart().getItems()) {
				ProductInfo product = productinfodao.findByName(item.getProductName());
				product.setProductStock(product.getProductStock() - item.getQuantity());
				productinfodao.save(product);
			}
			ShoppingOrder order = orderDao.findOrderById(id);
			order.setPaymnetId(razorpay_payment_id);
			order.setStatus("success");
			orderDao.saveOrder(order);
			customer.getCart().setItems(new ArrayList<Item>());
			customerdao.save(customer);
			session.setAttribute("customer", customerdao.findById(customer.getId()));
			session.setAttribute("successMessage", "Order Placed Success");
		
			return "redirect:/";
		}
	}

	@Override
	public String viewOrders(HttpSession session, ModelMap map, HttpServletResponse response) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			List<ShoppingOrder> orders = customer.getOrders();
			if (orders == null || orders.isEmpty()) {
				session.setAttribute("failMessage", "No Orders Yet");
				
				return "redirect:/";
			} else {
				map.put("orders", orders);
				return "ViewOrders";
			}
		}
	}

}
