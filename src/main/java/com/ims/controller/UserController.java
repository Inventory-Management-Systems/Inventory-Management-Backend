package com.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ims.Model.User;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.exception.ResourceNotFoundException;
import com.ims.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// Retrieve all employees

	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<User>> getAllEmployees() {
		try {
			List<User> allEmployees = userService.getAllEmployees();
			return ResponseEntity.ok(allEmployees);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Add a new employee

	@PostMapping("/addEmployee")
	public ResponseEntity<String> addEmployee(@RequestBody User employee) {
		try {
			boolean added = userService.addEmployee(employee);
			if (added) {
				return ResponseEntity.ok("Employee added successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add employee.");
			}
		} catch (DuplicateEmailException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate email found.");
		} catch (DuplicateMobileNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate mobile number found.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Update a existing employee

	@PutMapping("/updateEmployee")
	public ResponseEntity<String> updateEmployee(@RequestBody User employee) {
		try {
			boolean updated = userService.updateEmployee(employee, employee.getId());
			if (updated) {
				return ResponseEntity.ok("Employee updated successfully.");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (DuplicateEmailException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate email found.");
		} catch (DuplicateMobileNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate mobile number found.");
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred during employee update.");
		}
	}

	// Delete an employee

	@DeleteMapping("/deleteEmployee")
	public ResponseEntity<String> deleteEmployee(@RequestBody User user) {
		try {
			boolean deleted = userService.deleteEmployee(user);
			if (deleted) {
				return ResponseEntity.ok("Employee deleted successfully.");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Get the count of employees

	@GetMapping("/getEmployeesCount")
	public ResponseEntity<Integer> getEmployeesCount() {
		try {
			int count = userService.getEmployeeCount();
			return ResponseEntity.ok(count);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Get the count of Admin

	@GetMapping("/getAdminCount")
	public ResponseEntity<Integer> getAllAdminCount() {
		try {
			int adminCount = userService.getAllAdminCount();
			return ResponseEntity.ok(adminCount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
