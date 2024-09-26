package com.ims.service;

import java.util.List;

import com.ims.Model.User;

public interface UserService {
	public List<User> getAllEmployees();

	public int getEmployeeCount();

	public int getAllAdminCount();

	public boolean addEmployee(User employee);

	public boolean deleteEmployee(User employee);

	public boolean updateEmployee(User user, int id);

}
