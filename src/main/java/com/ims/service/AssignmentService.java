package com.ims.service;

import java.util.List;

import com.ims.Model.Item;

import dto.EmployeeItemDTO;

public interface AssignmentService {
	public List<String> getItemNamesByUserId(int userId);

	public List<Item> getAssignItemsByUserId(int userId);

	public int getAssignItemCount();

	public int getEmployeeAssignItemCount(int userId);

	public boolean assignItemsToEmployee(int userId, List<Integer> itemIds);

	public boolean unassignItemsFromEmployee(int userId, List<Integer> itemIds);

	public List<Item> getUnassignItemsByUserId(int userId);

	public List<EmployeeItemDTO> getEmployeesAndItems();

}
