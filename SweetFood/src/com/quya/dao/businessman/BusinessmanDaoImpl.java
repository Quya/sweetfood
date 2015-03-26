package com.quya.dao.businessman;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



import com.quya.dao.BaseDao;
import com.quya.model.Address;
import com.quya.model.Shop;
import com.quya.model.User;

public class BusinessmanDaoImpl extends BaseDao implements BusinessmanDao{
	
	@Override
	/**
	 * 必须判断是否是第一次修改，即从photoId判断是否已有图片插入到photo表中，美食店地址和简介也一样要判断是否为第一次修改
	 */
	public void updateInfo(User user) {
		// TODO Auto-generated method stub
		/**
		 * 更新地址
		 */
		System.out.println("start----");
		final Address address=user.getAddress();
		if(user.getAddress().getId()==0){
			final String addressSql="insert into tb_address (prov,city,dist,street) values(?,?,?,?)";
			 KeyHolder keyHolder=new GeneratedKeyHolder();//获取最新id 
			 getJdbcTemplate().update(
			            new PreparedStatementCreator() {//必须是java.sql.PreparedStatement 
							public java.sql.PreparedStatement createPreparedStatement(
									java.sql.Connection arg0)
									throws SQLException {
								// TODO Auto-generated method stub
								java.sql.PreparedStatement ps =  getJdbcTemplate().getDataSource()
			                            .getConnection().prepareStatement(addressSql,new String[]{ "prov" ,"city","dist","street"});
			                    ps.setString(1, address.getProvince());
				                  ps.setString(2, address.getCity());
				                  ps.setString(3, address.getDist());
				                  ps.setString(4, address.getStreet());
			                    return ps;								
							}
			            }, keyHolder);

		user.getAddress().setId(keyHolder.getKey().intValue() );
		System.out.println(keyHolder.getKey().intValue());
		}
		else{
			String addressSql="update tb_address set (prov,city,dist,street)values(?,?,?,?) where id=?";
			getJdbcTemplate().update(addressSql,new Object[] { address.getProvince(),address.getCity(),address.getDist(),address.getStreet(),address.getId()});
		}
		
		/**
		 * 更新头像
		 */
		if(user.getPhotoId()==0){
			String imgSql="insert into tb_photo (photo_ip)values(?)";
			getJdbcTemplate().update(imgSql,new Object[] { user.getPhotoUrl()});
			String findImgSql="select photo_id from tb_photo where photo_ip=?";
			List<Integer>id=getJdbcTemplate().query(findImgSql, new Object[] {user.getPhotoUrl() }, new RowMapper() {
		            public Object mapRow(ResultSet rs, int pRowNum) throws SQLException {
		            	Integer id=new Integer(rs.getInt("photo_id"));
		            	return id;
		            }
		        });
			user.setPhotoId(id.get(0));
		}else{
			String infoSql="update tb_user set(user_name,user_sex,user_email,user_phone) values(?,?,?,?) where user_id=?";
			getJdbcTemplate().update(infoSql,new Object[] {user.getName(),user.getSex(),user.getEmail(),user.getPhone(),user.getId()});
			String updatePhotoSql="update tb_photo set photo_ip=? where photo_id=?";
			getJdbcTemplate().update(updatePhotoSql,new Object[] {user.getPhotoUrl(),user.getPhotoUrl()});
		}
		
		/**
		 * 更新店铺信息
		 */
		String findShopSql="select * from tb_shop where businessman_id=?";
		List<Shop> shop=getJdbcTemplate().query(findShopSql, new Object[] {user.getId() }, new RowMapper() {
            public Object mapRow(ResultSet rs, int pRowNum) throws SQLException {
            	Shop shop=new Shop();
            	shop.setId(rs.getInt("id"));
            	shop.setAddressId(rs.getInt("address"));
            	shop.setBusinessmanId(rs.getInt("businessman_id"));
            	shop.setIndroduce(rs.getString("indroduce"));
            	return shop;
            }
        });
		if(shop.isEmpty()){
			String shopSql="insert into tb_shop(businessman_id,indroduce,address)values(?,?,?)";
			getJdbcTemplate().update(shopSql,new Object[] {user.getId(),user.getIndroduce(),user.getAddress().getId()});			
		}else{
			String updateShopSql="update tb_shop set(indroduce,address)values(?,?) where id=?";
			getJdbcTemplate().update(updateShopSql,new Object[] {user.getIndroduce(),user.getAddress().getId(),shop.get(0).getId()});			
		}
	}

}
