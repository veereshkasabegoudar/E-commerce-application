package com.veeresh.shopping.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.veeresh.shopping.dto.Item;
import com.veeresh.shopping.repository.itemRepository;

@Repository
public class Itemdao {
	@Autowired
	itemRepository itemRepository;

	public Item findById(int id) {
		return itemRepository.findById(id).orElseThrow();
	}

	public void delete(Item item) {
		 itemRepository.delete(item);
		
	}

	public void save(Item item) {
		 itemRepository.save(item);
		
	}

}
