package com.jw.remote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.jw.remote.db.DatabasePool;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.UserStasticEntity;

public class UserDao {
	
	private DatabasePool pool = DatabasePool.getInstance();
	
	private long queryLastInsertId(Connection conn) throws SQLException {
		if (conn != null) {
			java.sql.PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
				try {
					ResultSet rs = ps.executeQuery();
					try {
						if (rs.next())
							return rs.getShort(1);
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
			return -1;
	}
	/**
	 * 新增用户
	 * @param user
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void addUser(UserEntity user) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "insert into td_user(phone, password, limits, sex,zone,sign,prov,city,version) values(?,?,?,?,?,?,?,?,?)";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setString(1, user.getPhone());
				ps.setString(2, user.getPassword());
				ps.setInt(3, user.getLimits());
				ps.setInt(4, user.getSex());
				ps.setString(5, user.getZone());
				ps.setString(6, user.getSign());
				ps.setInt(7, user.getProv());
				ps.setInt(8, user.getCity());
				ps.setInt(9, user.getVersion());
				ps.execute();
			} finally {
				ps.close();
			}
			long id = queryLastInsertId(conn);
			user.setId(id);
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
	}
	
	/**
	 * 根据用户名查询
	 * @param phone
	 * @return
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public UserEntity getUser(String phone) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "select * from td_user where phone=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setString(1, phone);
				rs = ps.executeQuery();
				try {
					if (rs.next()) {
						UserEntity user = new UserEntity();
						user.setId(rs.getLong("id"));
						user.setPhone(rs.getString("phone"));
						user.setPassword(rs.getString("password"));
						user.setSex(rs.getInt("sex"));
						user.setUsername(rs.getString("username"));
						user.setLimits(rs.getInt("limits"));
						user.setWeixin(rs.getString("weixin"));
						user.setTouxiang(rs.getString("touxiang"));
						user.setZone(rs.getString("zone"));
						user.setSign(rs.getString("sign"));
						user.setProv(rs.getInt("prov"));
						user.setCity(rs.getInt("city"));
						user.setVersion(rs.getInt("version"));
						return user;
					}
				} finally {
					rs.close();
				}
			} finally {
				ps.close();
			}
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
		return null;
	}
	
	/**
	 * 删除用户
	 * @param phone
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void deleteUser(String phone) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "delete from td_user where phone=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setString(1, phone);
				ps.execute();
			} finally {
				ps.close();
			}
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @throws NotInitializedException 
	 * @throws TimeoutException 
	 * @throws SQLException 
	 */
	public void updateUser(UserEntity user) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "update td_user set";
		sql = sql +" sex="+user.getSex()+", prov="+user.getProv()+", city="+user.getCity();
		
		List<String> paras = new ArrayList<String>();
		
		if(user.getUsername() != null) {
			sql = sql + ",username=?";
			paras.add(user.getUsername());
		}
		if(user.getWeixin() != null) {
			sql = sql +",weixin=?";
			paras.add(user.getWeixin());
		}
		if(user.getTouxiang() != null) {
			sql = sql +",touxiang=?";
			paras.add(user.getTouxiang());
		}
		if(user.getZone() != null){
			sql = sql +",zone=?";
			paras.add(user.getZone());
		}
		if(user.getSign() != null){
			sql = sql +",sign=?";
			paras.add(user.getSign());
		}
		if(user.getVersion() != 0){
			sql = sql+",version="+user.getVersion();
		}
		sql = sql +" where phone=?";
		paras.add(user.getPhone());
		int size = paras.size();
		
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				
				for(int i=0;i<size;i++){
					ps.setString(i+1, paras.get(i));
				}
				ps.executeUpdate();
			} finally {
				ps.close();
			}
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
	}
	
	/**
	 * 按省份统计
	 * @return
	 * @throws SQLException
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 */
	public List<UserStasticEntity> getUserCountByProv() throws SQLException, TimeoutException, NotInitializedException{
		List<UserStasticEntity> list = new ArrayList<UserStasticEntity>();
		String sql = "select prov,count(*) from td_user group by prov ";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				rs = ps.executeQuery();
				try {
					while (rs.next()) {
						UserStasticEntity e = new UserStasticEntity();
						e.setProv(rs.getInt(1));
						e.setCount(rs.getInt(2));
						list.add(e);
					}
				} finally {
					rs.close();
				}
			} finally {
				ps.close();
			}
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
		return list;
	}
	
		public List<UserStasticEntity> getUserCountByProvCity(int prov) throws SQLException, TimeoutException, NotInitializedException{
			List<UserStasticEntity> list = new ArrayList<UserStasticEntity>();
			String sql = "select prov,city,count(*) from td_user group by prov,city having prov=?";
			Connection conn = pool.getConnection();
			java.sql.PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				try {
					ps.setInt(1, prov);
					rs = ps.executeQuery();
					try {
						while (rs.next()) {
							UserStasticEntity e = new UserStasticEntity();
							e.setProv(rs.getInt(1));
							e.setCity(rs.getInt(2));
							e.setCount(rs.getInt(3));
							list.add(e);
						}
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
			} catch (SQLException e) {
				conn.close();
				throw e;
			} finally {
				pool.release(conn);
			}
			return list;
		}
		
		public List<UserStasticEntity> getUserCountByProvCity(int prov,int city) throws SQLException, TimeoutException, NotInitializedException{
			List<UserStasticEntity> list = new ArrayList<UserStasticEntity>();
			String sql = "select prov,city,count(*) from td_user group by prov,city having prov=? and city = ?";
			Connection conn = pool.getConnection();
			java.sql.PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				try {
					ps.setInt(1, prov);
					ps.setInt(2, city);
					rs = ps.executeQuery();
					try {
						while (rs.next()) {
							UserStasticEntity e = new UserStasticEntity();
							e.setProv(rs.getInt(1));
							e.setCity(rs.getInt(2));
							e.setCount(rs.getInt(3));
							list.add(e);
						}
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
			} catch (SQLException e) {
				conn.close();
				throw e;
			} finally {
				pool.release(conn);
			}
			return list;
		}
		
		
		/**
		 * 获取 省份城市下用户列表
		 * @param prov
		 * @param city
		 * @return
		 * @throws SQLException
		 * @throws TimeoutException
		 * @throws NotInitializedException
		 */
		public List<UserEntity> getUserListByProvCity(int prov,int city,int index, int size ) throws SQLException, TimeoutException, NotInitializedException{
			List<UserEntity> list = new ArrayList<UserEntity>();
			int start = index*size;
			String sql = "select * from td_user where prov=? and city = ? limit ?,?";
			Connection conn = pool.getConnection();
			java.sql.PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				try {
					ps.setInt(1, prov);
					ps.setInt(2, city);
					ps.setInt(3, start);
					ps.setInt(4, size);
					rs = ps.executeQuery();
					try {
						while (rs.next()) {
							UserEntity user = new UserEntity();
							user.setId(rs.getLong("id"));
							user.setPhone(rs.getString("phone"));
//							user.setPassword(rs.getString("password"));
							user.setSex(rs.getInt("sex"));
							user.setUsername(rs.getString("username"));
//							user.setLimits(rs.getInt("limits"));
							user.setWeixin(rs.getString("weixin"));
//							user.setTouxiang(rs.getString("touxiang"));
							user.setZone(rs.getString("zone"));
							user.setSign(rs.getString("sign"));
							user.setProv(rs.getInt("prov"));
							user.setCity(rs.getInt("city"));
							user.setVersion(rs.getInt("version"));
							list.add(user);
						}
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
			} catch (SQLException e) {
				conn.close();
				throw e;
			} finally {
				pool.release(conn);
			}
			return list;
		}
//	/**
//	 * 根据用户名查询
//	 * @param userid
//	 * @return
//	 * @throws TimeoutException
//	 * @throws NotInitializedException
//	 * @throws SQLException
//	 */
//	public UserEntity getUser(String userid) throws TimeoutException, NotInitializedException, SQLException{
//		String sql = "select * from td_ms_user where userid=?";
//		Connection conn = pool.getConnection();
//		java.sql.PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			try {
//				ps.setString(1, userid);
//				rs = ps.executeQuery();
//				try {
//					if (rs.next()) {
//						UserEntity user = new UserEntity();
//						user.setId(rs.getLong("id"));
//						user.setUserId(rs.getString("userid"));
//						user.setPassword(rs.getString("password"));
//						user.setName(rs.getString("name"));
//						user.setSex(rs.getInt("sex"));
//						return user;
//					}
//				} finally {
//					rs.close();
//				}
//			} finally {
//				ps.close();
//			}
//		} catch (SQLException e) {
//			conn.close();
//			throw e;
//		} finally {
//			pool.release(conn);
//		}
//		return null;
//	}
//	
//	public UserEntity getUser(int id) throws TimeoutException, NotInitializedException, SQLException{
//		String sql = "select * from td_ms_user where id=?";
//		Connection conn = pool.getConnection();
//		java.sql.PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			try {
//				ps.setInt(1, id);
//				rs = ps.executeQuery();
//				try {
//					if (rs.next()) {
//						UserEntity user = new UserEntity();
//						user.setId(rs.getLong("id"));
//						user.setUserId(rs.getString("userid"));
//						user.setPassword(rs.getString("password"));
//						user.setName(rs.getString("name"));
//						user.setSex(rs.getInt("sex"));
//						return user;
//					}
//				} finally {
//					rs.close();
//				}
//			} finally {
//				ps.close();
//			}
//		} catch (SQLException e) {
//			conn.close();
//			throw e;
//		} finally {
//			pool.release(conn);
//		}
//		return null;
//	}
//	
//	public List<UserEntity> queryFriendUsers(int userid) throws TimeoutException, NotInitializedException, SQLException{
//		List<UserEntity> users = new ArrayList<UserEntity>();
//		String sql = "select * from td_ms_user where id in(select friendid from td_ms_friends where userid=?)";
//		Connection conn = pool.getConnection();
//		java.sql.PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			try {
//				ps.setInt(1, userid);
//				rs = ps.executeQuery();
//				try {
//					while (rs.next()) {
//						UserEntity user = new UserEntity();
//						user.setId(rs.getLong("id"));
//						user.setUserId(rs.getString("userid"));
////						user.setPassword(rs.getString("password"));
//						user.setName(rs.getString("name"));
//						user.setSex(rs.getInt("sex"));
//						users.add(user);
//					}
//				} finally {
//					rs.close();
//				}
//			} finally {
//				ps.close();
//			}
//		} catch (SQLException e) {
//			conn.close();
//			throw e;
//		} finally {
//			pool.release(conn);
//		}
//		
//		return users;
//	}

}
