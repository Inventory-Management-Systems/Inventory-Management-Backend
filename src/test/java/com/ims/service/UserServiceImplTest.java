package com.ims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.ims.Model.User;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.repository.AssignmentRepository;
import com.ims.repository.UserRepository;
import com.ims.service.impl.UserServiceImpl;

public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private AssignmentRepository assignmentRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllEmployees() {
		// Prepare mock data
		List<User> employees = new ArrayList();
		employees.add(new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee"));
		employees.add(new User(2, "Jane", "Smith", "jane@example.com", 28, null, 9876543210L, "password", "employee"));

		// Configure UserRepository mock
		when(userRepository.findByRoleIgnoreCase("employee", Sort.by(Sort.Direction.ASC, "fname")))
				.thenReturn(employees);

		// Call the method
		List<User> result = userService.getAllEmployees();

		// Verify the result
		assertEquals(employees, result);
		verify(userRepository).findByRoleIgnoreCase("employee", Sort.by(Sort.Direction.ASC, "fname"));
	}

	@Test
	public void testGetEmployeeCount() {
		// Prepare mock data
		List<User> employees = new ArrayList<>();
		employees.add(new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee"));
		employees.add(new User(2, "Jane", "Smith", "jane@example.com", 28, null, 9876543210L, "password", "employee"));

		// Configure UserRepository mock
		when(userRepository.findByRoleIgnoreCase("employee", Sort.by(Sort.Direction.ASC, "fname")))
				.thenReturn(employees);

		// Call the method
		int result = userService.getEmployeeCount();

		// Verify the result
		assertEquals(employees.size(), result);
		verify(userRepository).findByRoleIgnoreCase("employee", Sort.by(Sort.Direction.ASC, "fname"));
	}

	@Test
	public void testGetAllAdminCount() {
		// Prepare mock data
		List<User> admins = new ArrayList<>();
		admins.add(new User(1, "Admin", "Admin", "admin@example.com", 35, null, 9876543210L, "password", "admin"));
		admins.add(new User(2, "Super", "Admin", "superadmin@example.com", 40, null, 1234567890L, "password", "admin"));

		// Configure UserRepository mock
		when(userRepository.findByRoleIgnoreCase("Admin")).thenReturn(admins);

		// Call the method
		int result = userService.getAllAdminCount();

		// Verify the result
		assertEquals(admins.size(), result);
		verify(userRepository).findByRoleIgnoreCase("Admin");
	}

	@Test
	public void testAddEmployee_Success() {
		// Prepare mock data
		User employee = new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee");

		// Configure UserRepository mock
		when(userRepository.existsByEmailIgnoreCase(employee.getEmail())).thenReturn(false);
		when(userRepository.existsByMobile(employee.getMobile())).thenReturn(false);

		// Call the method
		boolean result = userService.addEmployee(employee);

		// Verify the result
		assertTrue(result);
		verify(userRepository).existsByEmailIgnoreCase(employee.getEmail());
		verify(userRepository).existsByMobile(employee.getMobile());
		verify(userRepository).save(employee);
	}

	@Test
	public void testAddEmployee_DuplicateEmail() {
		// Prepare mock data
		User employee = new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee");

		// Configure UserRepository mock
		when(userRepository.existsByEmailIgnoreCase(employee.getEmail())).thenReturn(true);

		// Call the method and assert the exception
		assertThrows(DuplicateEmailException.class, () -> userService.addEmployee(employee));

		// Verify the interactions
		verify(userRepository).existsByEmailIgnoreCase(employee.getEmail());
		verify(userRepository, never()).existsByMobile(employee.getMobile());
		verify(userRepository, never()).save(employee);
	}

	@Test
	public void testAddEmployee_DuplicateMobileNumber() {
		// Prepare mock data
		User employee = new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee");

		// Configure UserRepository mock
		when(userRepository.existsByEmailIgnoreCase(employee.getEmail())).thenReturn(false);
		when(userRepository.existsByMobile(employee.getMobile())).thenReturn(true);

		// Call the method and assert the exception
		assertThrows(DuplicateMobileNumberException.class, () -> userService.addEmployee(employee));

		// Verify the interactions
		verify(userRepository).existsByEmailIgnoreCase(employee.getEmail());
		verify(userRepository).existsByMobile(employee.getMobile());
		verify(userRepository, never()).save(employee);
	}

	@Test
	public void testDeleteEmployee_Success() {
		// Prepare mock data
		User employee = new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password", "employee");

		// Call the method
		boolean result = userService.deleteEmployee(employee);

		// Verify the result
		assertEquals(true, result);
		verify(assignmentRepository).deleteByUserId(employee.getId());
		verify(userRepository).delete(employee);
	}

	@Test
	public void testUpdateEmployee_Success() {
		// Prepare mock data
		User existingEmployee = new User(1, "John", "Doe", "john@example.com", 30, null, 1234567890L, "password",
				"employee");
		User updatedEmployee = new User(1, "John", "Smith", "john.smith@example.com", 30, null, 1234567890L, "password",
				"employee");

		// Configure UserRepository mock
		when(userRepository.findById(existingEmployee.getId())).thenReturn(java.util.Optional.of(existingEmployee));
		when(userRepository.existsByEmailIgnoreCase(updatedEmployee.getEmail())).thenReturn(false);
		when(userRepository.existsByMobile(updatedEmployee.getMobile())).thenReturn(false);

		// Call the method
		boolean result = userService.updateEmployee(updatedEmployee, existingEmployee.getId());

		// Verify the result
		assertEquals(true, result);
		verify(userRepository).findById(existingEmployee.getId());
		verify(userRepository).existsByEmailIgnoreCase(updatedEmployee.getEmail());
		verify(userRepository).existsByMobile(updatedEmployee.getMobile());
		verify(userRepository).save(existingEmployee);
	}
}
