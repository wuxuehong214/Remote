package com.jw.remote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.jw.remote.db.DatabasePool;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.GuanZhuEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.GuanZhuListEleEntity;

public class GuanZhuDao {
	
	private DatabasePool pool = DatabasePool.getInstance();
	
	/**
	 * 删除关注
	 * @param userid
	 * @param gzuserid
	 * @throws SQLException
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 */
	public void delGuanZhu(long gzid) throws SQLException, TimeoutException, NotInitializedException{
		String sql = "delete from td_gz where id=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, gzid);
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
	 * 根据关注人 被关注人查询关注记录
	 * @param userid
	 * @param gzuserid
	 * @return
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public GuanZhuEntity getGuanZhu(long userid, long gzuserid) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "select * from td_gz where userid=? and gzuserid=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, userid);
				ps.setLong(2, gzuserid);
				rs = ps.executeQuery();
				try {
					if (rs.next()) {
						GuanZhuEntity gz = new GuanZhuEntity();
						gz.setId(rs.getLong("id"));
						gz.setUserid(rs.getLong("userid"));
						gz.setGzuserid(rs.getLong("gzuserid"));
						gz.setState(rs.getInt("state"));
						return gz;
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
	 * 添加关注记录
	 * @param gz
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void addGuanZhu(GuanZhuEntity gz) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "insert into td_gz(userid,gzuserid,state) values(?,?,?)";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, gz.getUserid());
				ps.setLong(2, gz.getGzuserid());
				ps.setInt(3, gz.getState());
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
	
	public static void main(String args[]) throws TimeoutException, NotInitializedException, SQLException{
		GuanZhuDao dao = new GuanZhuDao();
		
		List<GuanZhuListEleEntity> list = dao.getGuanZhus(36);
		
		for(GuanZhuListEleEntity gz:list)
			System.out.println(gz.getId());
	}
	
	/**
	 * 查询  userid所关注的用户列表
	 * @param userid
	 * @return
	 * @throws NotInitializedException 
	 * @throws TimeoutException 
	 * @throws SQLException 
	 */
	public List<GuanZhuListEleEntity> getGuanZhus(long userid) throws TimeoutException, NotInitializedException, SQLException{
		List<GuanZhuListEleEntity> list = new ArrayList<GuanZhuListEleEntity>();
		String sql;
		sql = "select a.*,b.* from td_gz a, td_user b where a.gzuserid=b.id and a.userid=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, userid);
				rs = ps.executeQuery();
				try {
					while (rs.next()) {
						GuanZhuListEleEntity gz = new GuanZhuListEleEntity();
						UserEntity user = new com.jw.remote.entity.UserEntity();
						gz.setUser(user);
						
						gz.setId(rs.getLong("a.id"));
						user.setId(rs.getLong("b.id"));
						user.setUsername(rs.getString("username"));
						user.setPhone(rs.getString("phone"));
						user.setWeixin(rs.getString("weixin"));
						user.setZone(rs.getString("zone"));
						user.setSex(rs.getInt("sex"));
						user.setSign(rs.getString("sign"));
						user.setProv(rs.getInt("prov"));
						user.setCity(rs.getInt("city"));
						list.add(gz);
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

}
