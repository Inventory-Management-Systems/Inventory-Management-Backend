package com.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ims.Model.Item;
import com.ims.exception.DuplicateBillNumberException;
import com.ims.exception.DuplicateItemNameException;
import com.ims.exception.DuplicateSerialNumberException;
import com.ims.exception.ResourceNotFoundException;
import com.ims.service.ItemService;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	// Retrieve all items

	@GetMapping("/getAllItems")
	public ResponseEntity<List<Item>> getAllItems() {
		try {
			List<Item> allItems = itemService.getAllItems();
			return ResponseEntity.ok(allItems);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Add a new item

	@PostMapping("/addItem")
	public ResponseEntity<String> addItem(@RequestBody Item item) {
		try {
			boolean added = itemService.addItem(item);
			if (added) {
				return ResponseEntity.ok("Item added successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item.");
			}
		} catch (DuplicateSerialNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate serial number found.");
		} catch (DuplicateBillNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate bill number found.");
		} catch (DuplicateItemNameException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate item name found.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Update an existing item

	@PutMapping("/updateItem")
	public ResponseEntity<String> updateItem(@RequestBody Item item) {
		try {
			boolean updated = itemService.updateItem(item, item.getId());
			if (updated) {
				return ResponseEntity.ok("Item updated successfully.");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (DuplicateBillNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate bill number found.");
		} catch (DuplicateSerialNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate serial number found.");
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
		} catch (DuplicateItemNameException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate item name found.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Delete an item

	@DeleteMapping("/deleteItem")
	public ResponseEntity<String> deleteItem(@RequestBody Item item) {
		try {
			boolean deleted = itemService.deleteItem(item);
			if (deleted) {
				return ResponseEntity.ok("Item deleted successfully.");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Get the count of items

	@GetMapping("/getItemCount")
	public ResponseEntity<Integer> getItemCount() {
		try {
			int itemCount = itemService.getItemCount();
			return ResponseEntity.ok(itemCount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
