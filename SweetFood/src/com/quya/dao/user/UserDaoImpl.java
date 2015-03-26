package com.quya.dao.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.quya.dao.BaseDao;
import com.quya.model.User;

public class UserDaoImpl extends BaseDao implements UserDao{

	@Override
	public boolean isEmailRegisted(String email) {
		 List<User> list = getUserByEmail(email);
	        if (list.isEmpty()) {
	            return false;
	        }
		return true;
	}

	private List<User> getUserByEmail(String email) {
		String sql = "select * from tb_user where user_email = ?";
	        List<User> list = getJdbcTemplate().query(sql, new Object[] {email }, new RowMapper() {
	            public Object mapRow(ResultSet rs, int pRowNum) throws SQLException {
	            	User user=new User();
	            	user.setId(rs.getInt("user_id"));
	            	                return user;
	            }
	        });
		return list;
	}

	@Override
	public void regist(User user) {
		// TODO Auto-generated method stub
		String sql ="insert into tb_user (user_name,user_password,user_email,user_credits,user_sex,user_phone,user_power)values(?,?,?,?,?,?,?)";
		getJdbcTemplate().update(
				sql,
				new Object[] { user.getName(),user.getPassword(),user.getEmail(),user.getCredits(),user.getSex(),user.getPhone(),user.getPower()});
		List<User> list = getUserByEmail(user.getEmail());
		user.setId(list.get(0).getId());		
	}
}
