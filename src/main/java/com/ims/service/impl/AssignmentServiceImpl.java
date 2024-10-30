package com.ims.service.impl;

import com.ims.Model.Assignment;
import com.ims.Model.Item;
import com.ims.Model.User;
import com.ims.exception.UserNotFoundException;
import com.ims.repository.AssignmentRepository;
import com.ims.repository.ItemRepository;
import com.ims.repository.UserRepository;
import com.ims.service.AssignmentService;
import com.ims.service.ItemService;

import com.ims.dto.EmployeeItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	// Get item names assigned to an employee

	@Override
	public List<String> getItemNamesByUserId(int userId) {
		try {
			List<Assignment> assignments = assignmentRepository.findByUserId(userId);
			List<String> itemNames = new ArrayList<>();

			for (Assignment assignment : assignments) {
				itemNames.add(assignment.getItem().getName());
			}

			return itemNames;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Collections.emptyList();
		}
	}

	// Get item details which are assigned to specific employee

	@Override
	public List<Item> getAssignItemsByUserId(int userId) {
		try {
			List<Assignment> assignments = assignmentRepository.findByUserId(userId);
			List<Item> items = new ArrayList<>();

			for (Assignment assignment : assignments) {
				items.add(assignment.getItem());
			}

			// Sort items by their IDs
			Collections.sort(items, new Comparator<Item>() {
				@Override
				public int compare(Item item1, Item item2) {
					return item1.getId() - item2.getId();
				}
			});

			return items;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Collections.emptyList();
		}
	}

	// Get item details which are not assigned to specific employee

	@Override
	public List<Item> getUnassignItemsByUserId(int userId) {
		try {
			List<Item> items = new ArrayList<>();

			// Get all items
			List<Item> allItems = itemService.getAllItems();

			// Get assigned items for the given user
			List<Item> assignedItems = getAssignItemsByUserId(userId);

			// Get unassigned items by removing assigned items from all items
			items.addAll(allItems);
			items.removeAll(assignedItems);

			return items;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Collections.emptyList();
		}
	}

	// Get total count of assigned items

	@Override
	public int getAssignItemCount() {
		try {
			// Get all assignments
			List<Assignment> assignments = assignmentRepository.findAll();

			// Get the count of assignments
			int count = (int) assignments.stream().count();
			return count;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	// Get count of assigned items for a specific employee

	@Override
	public int getEmployeeAssignItemCount(int userId) {
		try {
			// Get assigned items for the given user
			List<Item> itemsByUserId = getAssignItemsByUserId(userId);
			return itemsByUserId.size();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	// Assign items to an employee

	@Override
	public boolean assignItemsToEmployee(int userId, List<Integer> itemIds) {
		try {
			// Find the user by ID or throw UserNotFoundException
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

			// Find items by their IDs
			List<Item> items = itemRepository.findAllById(itemIds);

			boolean itemsAssignedSuccessfully = true;

			// Assign each item to the user
			for (Item item : items) {
				try {
					Assignment assignment = new Assignment();
					assignment.setUser(user);
					assignment.setItem(item);
					assignmentRepository.save(assignment);
				} catch (Exception e) {
					itemsAssignedSuccessfully = false;
					System.out.println(e.getMessage());
					break;
				}
			}

			return itemsAssignedSuccessfully;
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while assigning items", e);
		}
	}

	// Unassign items from an employee

	@Override
	public boolean unassignItemsFromEmployee(int userId, List<Integer> itemIds) {
		try {
			// Find the user by ID or throw UserNotFoundException
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

			boolean itemsUnassignedSuccessfully = true;

			// Unassign each item from the user
			for (Integer itemId : itemIds) {
				try {
					Assignment assignment = assignmentRepository.findByUserAndItemId(user, itemId);
					if (assignment != null) {
						assignmentRepository.delete(assignment);
					}
				} catch (Exception e) {
					itemsUnassignedSuccessfully = false;
					System.out.println(e.getMessage());
					break;
				}
			}

			return itemsUnassignedSuccessfully;
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while unassigning items", e);
		}
	}

	// Get employees and their assigned items

	@Override
	public List<EmployeeItemDTO> getEmployeesAndItems() {
		try {
			// Get all assignments
			List<Assignment> assignments = assignmentRepository.findAll();

			List<EmployeeItemDTO> employeeItems = new ArrayList<>();

			// Create DTO objects for each assignment
			for (Assignment assignment : assignments) {
				User user = assignment.getUser();
				Item item = assignment.getItem();

				EmployeeItemDTO employeeItem = new EmployeeItemDTO();
				employeeItem.setEmployeeName(user.getFname() + " " + user.getLname());
				employeeItem.setItemName(item.getName());

				employeeItems.add(employeeItem);
			}

			return employeeItems;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while fetching employee items.", e);
		}
	}

}
