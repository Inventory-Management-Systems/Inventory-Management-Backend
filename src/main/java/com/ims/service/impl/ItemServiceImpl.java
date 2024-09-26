package com.ims.service.impl;

import com.ims.Model.Item;
import com.ims.exception.DuplicateBillNumberException;
import com.ims.exception.DuplicateItemNameException;
import com.ims.exception.DuplicateSerialNumberException;
import com.ims.exception.ResourceNotFoundException;
import com.ims.repository.AssignmentRepository;
import com.ims.repository.ItemRepository;
import com.ims.service.ItemService;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private AssignmentRepository assignmentRepository;

	// Get all items
	@Override
	public List<Item> getAllItems() {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		return itemRepository.findAll(sort);
	}

	// Get the count of items
	@Override
	public int getItemCount() {
		try {
			List<Item> temp = getAllItems();
			return temp.size();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while getting the item count.", e);
		}
	}

	// Add an item
	@Override
	public boolean addItem(Item item) {
		try {
			// Check if the item name already exists
			if (itemRepository.existsByName(item.getName())) {
				throw new DuplicateItemNameException("Duplicate item name found.");
			}

			// Check if the serial number already exists
			if (itemRepository.existsBySerialNumber(item.getSerialNumber())) {
				throw new DuplicateSerialNumberException("Duplicate serial number found.");
			}

			// Check if the bill number already exists
			if (itemRepository.existsByBillNumber(item.getBillNumber())) {
				throw new DuplicateBillNumberException("Duplicate bill number found.");
			}
			itemRepository.save(item);
			return true;
		} catch (DuplicateSerialNumberException | DuplicateBillNumberException | DuplicateItemNameException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while adding the item.", e);
		}
	}

	// Delete an item
	@Transactional
	@Override
	public boolean deleteItem(Item item) {
		try {
			assignmentRepository.deleteByItemId(item.getId());
			itemRepository.delete(item);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while deleting the item.", e);
		}
	}

	// Update an item
	@Override
	public boolean updateItem(Item item, int id) {
		try {
			Item existingItem = itemRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Item", "Id", id));

			// Check if the updated item name already exists
			if (!existingItem.getName().equals(item.getName()) && itemRepository.existsByName(item.getName())) {
				throw new DuplicateItemNameException("Duplicate item name found.");
			}

			// Check if the updated bill number already exists
			if (!existingItem.getBillNumber().equals(item.getBillNumber())
					&& itemRepository.existsByBillNumber(item.getBillNumber())) {
				throw new DuplicateBillNumberException("Duplicate bill number found.");
			}

			// Check if the updated serial number already exists
			if (!existingItem.getSerialNumber().equals(item.getSerialNumber())
					&& itemRepository.existsBySerialNumber(item.getSerialNumber())) {
				throw new DuplicateSerialNumberException("Duplicate serial number found.");
			}

			// Update the item
			existingItem.setName(item.getName());
			existingItem.setCategory(item.getCategory());
			existingItem.setWarranty(item.getWarranty());
			existingItem.setBillNumber(item.getBillNumber());
			existingItem.setSerialNumber(item.getSerialNumber());
			existingItem.setDateOfPurchase(item.getDateOfPurchase());
			itemRepository.save(existingItem);

			return true;
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (DuplicateBillNumberException | DuplicateSerialNumberException | DuplicateItemNameException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while updating the item.", e);
		}
	}

}
