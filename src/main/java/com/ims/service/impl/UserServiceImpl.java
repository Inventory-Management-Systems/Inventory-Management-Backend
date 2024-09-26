package com.ims.service.impl;

import com.ims.Model.User;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.exception.ResourceNotFoundException;
import com.ims.repository.AssignmentRepository;
import com.ims.repository.UserRepository;
import com.ims.service.UserService;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	// Get all employees
	@Override
	public List<User> getAllEmployees() {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "fname"); // Ordering by firstName in ascending order
			return userRepository.findByRoleIgnoreCase("employee", sort);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while retrieving employees.", e);
		}
	}

	// Get the count of employees
	@Override
	public int getEmployeeCount() {
		try {
			List<User> employees = getAllEmployees();
			return employees.size();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while retrieving the employee count.", e);
		}
	}

	// Get the count of all admins
	@Override
	public int getAllAdminCount() {
		try {
			List<User> temp = userRepository.findByRoleIgnoreCase("Admin");
			return temp.size();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while retrieving the admin count.", e);
		}
	}

	// Add an employee
	@Override
	public boolean addEmployee(User employee) {
		try {
			// Check if the email already exists
			if (userRepository.existsByEmailIgnoreCase(employee.getEmail())) {
				throw new DuplicateEmailException("Duplicate email found.");
			}

			// Check if the mobile number already exists
			if (userRepository.existsByMobile(employee.getMobile())) {
				throw new DuplicateMobileNumberException("Duplicate mobile number found.");
			}

			String encryptPassword = AuthServiceImpl.encryptPassword(employee.getPassword());
			employee.setPassword(encryptPassword);

			userRepository.save(employee);
			return true;
		} catch (DuplicateEmailException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (DuplicateMobileNumberException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while adding the employee.", e);
		}
	}

	// Delete an employee
	@Transactional
	@Override
	public boolean deleteEmployee(User employee) {
		try {
			assignmentRepository.deleteByUserId(employee.getId());
			userRepository.delete(employee);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while deleting the employee.", e);
		}
	}

	// Update an employee
	@Override
	public boolean updateEmployee(User user, int id)
			throws ResourceNotFoundException, DuplicateEmailException, DuplicateMobileNumberException {
		User existingEmployee = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", id));

		// Check if the updated email already exists
		if (!existingEmployee.getEmail().equals(user.getEmail())
				&& userRepository.existsByEmailIgnoreCase(user.getEmail())) {
			throw new DuplicateEmailException("Duplicate email found.");
		}

		// Check if the updated mobile number already exists
		if (existingEmployee.getMobile() != user.getMobile() && userRepository.existsByMobile(user.getMobile())) {
			throw new DuplicateMobileNumberException("Duplicate mobile number found.");
		}

		existingEmployee.setFname(user.getFname());
		existingEmployee.setLname(user.getLname());
		existingEmployee.setEmail(user.getEmail());
		existingEmployee.setAge(user.getAge());
		existingEmployee.setPassword(user.getPassword());
		existingEmployee.setDob(user.getDob());
		existingEmployee.setMobile(user.getMobile());
		existingEmployee.setRole(user.getRole());

		userRepository.save(existingEmployee);
		return true;
	}

}
