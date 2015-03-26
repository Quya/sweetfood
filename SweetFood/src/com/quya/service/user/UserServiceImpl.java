package com.quya.service.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.quya.common.utils.Md5PasswordEncryption;
import com.quya.common.utils.exception.BusinessException;
import com.quya.dao.user.UserDao;
import com.quya.model.User;

public class UserServiceImpl implements UserService{


	private UserDao userDao;
	private  Md5PasswordEncryption encryption=new Md5PasswordEncryption();
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void regist(User user) {
		// TODO Auto-generated method stub
		String encryptionPassword=encryption.encrypt(user.getPassword());
		user.setPassword(encryptionPassword);
		boolean isEmailRegisted=userDao.isEmailRegisted(user.getEmail());
		if(isEmailRegisted){
			throw new BusinessException("该邮箱已经被注册过，请使用新的邮箱",0);
		}else{
			userDao.regist(user);
		}
	}

	
	



}
