package com.ims.repository;

import com.ims.Model.Assignment;
import com.ims.Model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
	// Find assignments by user ID
	List<Assignment> findByUserId(int userId);

	// Delete assignments by user ID
	void deleteByUserId(int userId);

	// Delete assignments by item ID
	void deleteByItemId(int id);

	// Find assignment by user and item
	Assignment findByUserAndItemId(User user, int itemId);
}
