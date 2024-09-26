package com.ims.controller;

import com.ims.Model.User;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.exception.InvalidEmailOrPasswordException;
import com.ims.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthController {
	@Autowired
	private AuthService userService;

	
	// Register a new user

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		try {
			boolean registered = userService.registerUser(user);
			if (registered) {
				return ResponseEntity.ok("User registered successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
			}
		} catch (DuplicateEmailException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
		} catch (DuplicateMobileNumberException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile number already exists.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	// Log in a user

	@PostMapping("/login")
	public ResponseEntity<Object> userLogin(@RequestBody User user) {
		try {
			User loggedInUser = userService.loginUser(user);
			if (loggedInUser != null) {
				return ResponseEntity.ok(loggedInUser);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} catch (InvalidEmailOrPasswordException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
