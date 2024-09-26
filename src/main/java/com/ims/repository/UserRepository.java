package com.ims.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ims.Model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	// Find a user by email and password
	User findByEmailIgnoreCaseAndPassword(String email, String hashedPassword);

	// Find users by role with sorting
	List<User> findByRoleIgnoreCase(String string, Sort sort);

	// Find users by role
	List<User> findByRoleIgnoreCase(String string);

	// Check if a user with the given email exists
	boolean existsByEmailIgnoreCase(String email);

	// Check if a user with the given mobile number exists
	boolean existsByMobile(long mobile);
}
