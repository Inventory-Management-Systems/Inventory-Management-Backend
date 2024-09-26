package com.ims.service.impl;

import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.Model.User;
import com.ims.exception.DuplicateEmailException;
import com.ims.exception.DuplicateMobileNumberException;
import com.ims.exception.InvalidEmailOrPasswordException;
import com.ims.repository.UserRepository;
import com.ims.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean registerUser(User user) {
		try {
			// Check if email already exists
			if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
				throw new DuplicateEmailException("Email already exists.");
			}

			// Check if mobile number already exists
			if (userRepository.existsByMobile(user.getMobile())) {
				throw new DuplicateMobileNumberException("Mobile number already exists.");
			}

			// Encrypt password and save the user
			String encryptedPassword = encryptPassword(user.getPassword());
			user.setPassword(encryptedPassword);
			userRepository.save(user);
			return true;
		} catch (DuplicateEmailException | DuplicateMobileNumberException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while registering the user.", e);
		}
	}

	@Override
	public User loginUser(User userLogin) throws InvalidEmailOrPasswordException {
		try {
			String hashedPassword = encryptPassword(userLogin.getPassword());
			User user = userRepository.findByEmailIgnoreCaseAndPassword(userLogin.getEmail(), hashedPassword);

			if (user == null) {
				throw new InvalidEmailOrPasswordException("Invalid email or password.");
			}

			return user;
		} catch (InvalidEmailOrPasswordException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("An error occurred while logging in the user.", e);
		}
	}

	// Encrypted password to hash value

	public static String encryptPassword(String password) {
		String encrypted = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] passwordBytes = password.getBytes();

			digest.reset();
			digest.update(passwordBytes);
			byte[] message = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < message.length; i++) {
				hexString.append(Integer.toHexString(0xFF & message[i]));
			}
			encrypted = hexString.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return encrypted;
	}
}
