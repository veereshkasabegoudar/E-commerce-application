package com.veeresh.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veeresh.shopping.dto.Item;

public interface itemRepository extends JpaRepository<Item, Integer>{

}
