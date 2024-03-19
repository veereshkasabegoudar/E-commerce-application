package com.veeresh.shopping.service.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.veeresh.shopping.dao.Customerdao;
import com.veeresh.shopping.dao.Productinfodao;
import com.veeresh.shopping.dto.Customer;
import com.veeresh.shopping.dto.ProductInfo;
import com.veeresh.shopping.helper.AES;
import com.veeresh.shopping.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class AdminServiceimplementaion implements AdminService {
	@Autowired
	Productinfodao productinfodao;
	@Autowired
	ProductInfo productInfo;
	@Autowired
	Customerdao customerdao;

	@Override
	public String loadDashboard(HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			if (customer.getRole().equals("ADMIN"))
				return "AdminDashBoard";
			else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String Addproduct(HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			if (customer.getRole().equals("ADMIN")) {
				map.put("productInfo", productInfo);
				return "Addproduct";
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String addproduct(ProductInfo productInfo, BindingResult result, ModelMap map, HttpSession session,
			MultipartFile picture) {

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {

			if (customer.getRole().equals("ADMIN")) {
				if (productinfodao.chechname(productInfo.getProductName()))
					result.rejectValue("productName", "error.productName", "Product with Same Name Already Exists");

				if (result.hasErrors()) {

					return "AddProduct";
				} else {
					productInfo.setImagePath("/images/" + productInfo.getProductName() + ".jpg");
					productinfodao.save(productInfo);

					File file = new File("src/main/resources/static/images");
					if (!file.isDirectory())
						file.mkdir();

					try {
						Files.write(
								Paths.get("src/main/resources/static/images", productInfo.getProductName() + ".jpg"),
								picture.getBytes());
					} catch (IOException e) {
						session.setAttribute("failMessage", "You are Unauthorized to access his URL");
						return "redirect:/";
					}
					session.setAttribute("successMessage", "Product Added Success");
					return "redirect:/admin";
				}
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String manageproducts(HttpSession session, ModelMap map) {

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			if (customer.getRole().equals("ADMIN")) {
				List<ProductInfo> products = productinfodao.fetchAll();
				if (products.isEmpty()) {
					session.setAttribute("failMessage", "No Products found");
					return "redirect:/admin";
				} else {
					map.put("products", products);
					return "ManageProducts";
				}
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String deleteproduct(int id, HttpSession session) {

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			if (customer.getRole().equals("ADMIN")) {
				ProductInfo product = productinfodao.findById(id);
				File file = new File("src/main/resources/static" + product.getImagePath());
				if (file.exists())
					file.delete();
				productinfodao.delete(product);
				session.setAttribute("successMessage", "Product Deleted Success");
				return "redirect:/admin/manage-products";
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}

	}

	@Override
	public String editProduct(int id, HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {
			if (customer.getRole().equals("ADMIN")) {
				System.out.println("*******2********");
				ProductInfo product = productinfodao.findById(id);
				map.put("product", product);
				System.out.println("*******3********");
				return "EditProduct";
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}

	}

	@Override
	public String updateProduct(@Valid ProductInfo productInfo, BindingResult result, MultipartFile picture,
			HttpSession session, ModelMap map) {

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/signin";
		} else {

			if (customer.getRole().equals("ADMIN")) {

				if (result.hasErrors())

					return "EditProduct";
				else {
					productInfo.setImagePath("/images/" + productInfo.getProductName() + ".jpg");
					productinfodao.save(productInfo);

					File file = new File("src/main/resources/static/images");
					if (!file.isDirectory())
						file.mkdir();

					try {
						Files.write(
								Paths.get("src/main/resources/static/images", productInfo.getProductName() + ".jpg"),
								picture.getBytes());

					} catch (IOException e) {
						session.setAttribute("failMessage", "You are Unauthorized to access his URL");
						return "redirect:/";
					}
					session.setAttribute("successMessage", "Product Updated Success");
					return "redirect:/admin";
				}
			} else {
				session.setAttribute("failMessage", "You are Unauthorized to access his URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String createAdmin(String email, String password, HttpSession session) {
		if (!customerdao.checkEmailDuplicate(email)) {
			Customer customer = new Customer();
			customer.setEmail(email);
			customer.setPassword(AES.encrypt(password, "123"));
			customer.setRole("ADMIN");
			customer.setVerified(true);
			customerdao.save(customer);
			session.setAttribute("successMessage", "Admin Account creation success");
			return "redirect:/";
		} else {
			session.setAttribute("failMessage", "Admin Account Already Exists");
			return "redirect:/";
		}
	}
}
