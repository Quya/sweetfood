package com.quya.service.businessman;

import com.quya.dao.businessman.BusinessmanDao;
import com.quya.model.User;

public class BusinessmanServiceImpl implements BusinessmanService {

	private BusinessmanDao businessmanDao;

	public BusinessmanDao getBusinessmanDao() {
		return businessmanDao;
	}

	public void setBusinessmanDao(BusinessmanDao businessmanDao) {
		this.businessmanDao = businessmanDao;
	}

	@Override
	public void updateInfo(User user) {
		// TODO Auto-generated method stub
		businessmanDao.updateInfo(user);
	}
}
