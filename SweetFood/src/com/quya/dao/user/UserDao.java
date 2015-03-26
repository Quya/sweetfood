package com.quya.dao.user;

import com.quya.model.User;

public interface UserDao {

	boolean isEmailRegisted(String email);

	void regist(User user);

}
