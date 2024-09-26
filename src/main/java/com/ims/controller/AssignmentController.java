package com.ims.controller;

import com.ims.Model.Item;
import com.ims.exception.UserNotFoundException;
import com.ims.service.AssignmentService;

import dto.EmployeeItemDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/assignment")
public class AssignmentController {
	@Autowired
	private AssignmentService assignmentService;

	// Get item names assigned to an employee

	@GetMapping("/getEmployeeAssignItemNames/{userId}")
	public List<String> getItemNamesByUserId(@PathVariable("userId") int userId) {
		return assignmentService.getItemNamesByUserId(userId);
	}

	// Get item details which are assigned to specific employee

	@GetMapping("/getEmployeeAssignItems/{userId}")
	public List<Item> getAssignItemsByUserId(@PathVariable("userId") int userId) {
		return assignmentService.getAssignItemsByUserId(userId);
	}

	// Get item details which are not assigned to specific employee

	@GetMapping("/getEmployeeUnassignItems/{userId}")
	public List<Item> getUnassignItemsByUserId(@PathVariable("userId") int userId) {
		return assignmentService.getUnassignItemsByUserId(userId);
	}

	// Get total count of assigned items

	@GetMapping("/getTotalAssignmentCount")
	public int getAssignItemCount() {
		return assignmentService.getAssignItemCount();
	}

	// Get count of assigned items for a specific employee

	@GetMapping("/getEmployeeAssignItemCount/{userId}")
	public int getEmployeeAssignItemCount(@PathVariable("userId") int userId) {
		return assignmentService.getEmployeeAssignItemCount(userId);
	}

	// Assign items to an employee

	@PostMapping("/assignItemsToEmployee/{userId}")
	public ResponseEntity<String> assignItemsToEmployee(@PathVariable("userId") int userId,
			@RequestBody List<Integer> itemIds) {
		try {
			boolean itemsAssignedSuccessfully = assignmentService.assignItemsToEmployee(userId, itemIds);
			if (itemsAssignedSuccessfully) {
				return ResponseEntity.ok("Items assigned successfully");
			} else {
				return ResponseEntity.badRequest().body("Failed to assign items");
			}
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().body("Invalid user");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Unassign items from an employee

	@PostMapping("/unassignItemsFromEmployee/{userId}")
	public ResponseEntity<String> unassignItemsFromEmployee(@PathVariable("userId") int userId,
			@RequestBody List<Integer> itemIds) {
		try {
			boolean itemsUnassignedSuccessfully = assignmentService.unassignItemsFromEmployee(userId, itemIds);
			if (itemsUnassignedSuccessfully) {
				return ResponseEntity.ok("Items unassigned successfully");
			} else {
				return ResponseEntity.badRequest().body("Failed to unassign items");
			}
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().body("Invalid user");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Get employees and their assigned items

	@GetMapping("/getEmployeesAndItems")
	public ResponseEntity<List<EmployeeItemDTO>> getEmployeesAndItems() {
		try {
			List<EmployeeItemDTO> employeeItems = assignmentService.getEmployeesAndItems();
			return ResponseEntity.ok(employeeItems);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
