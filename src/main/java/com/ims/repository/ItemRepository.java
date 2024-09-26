package com.ims.repository;

import com.ims.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	// Find an item by ID
	Item findById(Item item);

	// Check if an item with the given serial number exists
	boolean existsBySerialNumber(String serialNumber);

	// Check if an item with the given bill number exists
	boolean existsByBillNumber(String billNumber);

	// Check if an item with the given name exists
	boolean existsByName(String name);
}
