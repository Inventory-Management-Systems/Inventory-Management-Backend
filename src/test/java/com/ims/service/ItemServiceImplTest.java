package com.ims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ims.Model.Item;
import com.ims.Model.User;
import com.ims.controller.ItemController;
import com.ims.exception.DuplicateBillNumberException;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateItemNameException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.exception.DuplicateSerialNumberException;
import com.ims.repository.AssignmentRepository;
import com.ims.repository.UserRepository;
import com.ims.service.impl.UserServiceImpl;

public class ItemServiceImplTest {

	@Mock
	private ItemService itemService;

	@InjectMocks
	private ItemController itemController;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddItem_Success() {
		// Prepare mock data
		Item item = new Item(1, "Laptop", "Electronics", "SN123", "BN456", LocalDate.now(), "1 year warranty");

		// Configure ItemService mock
		when(itemService.addItem(item)).thenReturn(true);

		  // Call the method
        ResponseEntity<?> response = itemController.addItem(item);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemService).addItem(item);
	}

	@Test
	public void testAddItem_DuplicateItemName() {
		// Prepare mock data
		Item item = new Item(1, "Laptop", "Electronics", "SN123", "BN456", LocalDate.now(), "1 year warranty");

		// Configure ItemService mock
		when(itemService.addItem(item)).thenThrow(new DuplicateItemNameException("Duplicate item name found."));

		// Call the method and assert the exception
		assertThrows(DuplicateItemNameException.class, () -> itemController.addItem(item));

		// Verify the interactions
		verify(itemService).addItem(item);
	}

	@Test
	public void testAddItem_DuplicateSerialNumber() {
		// Prepare mock data
		Item item = new Item(1, "Laptop", "Electronics", "SN123", "BN456", LocalDate.now(), "1 year warranty");

		// Configure ItemService mock
		when(itemService.addItem(item)).thenThrow(new DuplicateSerialNumberException("Duplicate serial number found."));

		// Call the method and assert the exception
		assertThrows(DuplicateSerialNumberException.class, () -> itemController.addItem(item));

		// Verify the interactions
		verify(itemService).addItem(item);
	}

	@Test
	public void testAddItem_DuplicateBillNumber() {
		// Prepare mock data
		Item item = new Item(1, "Laptop", "Electronics", "SN123", "BN456", LocalDate.now(), "1 year warranty");

		// Configure ItemService mock
		when(itemService.addItem(item)).thenThrow(new DuplicateBillNumberException("Duplicate bill number found."));

		// Call the method and assert the exception
		assertThrows(DuplicateBillNumberException.class, () -> itemController.addItem(item));

		// Verify the interactions
		verify(itemService).addItem(item);
	}

}
