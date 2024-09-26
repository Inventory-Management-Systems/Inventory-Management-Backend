package com.ims.service;

import com.ims.Model.User;

public interface AuthService {
	public boolean registerUser(User user);

	public User loginUser(User userLogin);
}
