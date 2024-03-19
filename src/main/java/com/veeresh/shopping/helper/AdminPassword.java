package com.veeresh.shopping.helper;

public class AdminPassword {
	public static void main(String[] args) {
		System.out.println(AES.encrypt("admin", "123"));
	}
}
