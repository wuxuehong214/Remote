package com.jw.remote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.jw.remote.db.DatabasePool;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.TopicEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.TopicListEleEntity;

public class TopicDao {
	
	private DatabasePool pool = DatabasePool.getInstance();
	
	/**
	 * 获取topic
	 * @param id
	 * @return
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public TopicListEleEntity getTopic(long id)throws TimeoutException,NotInitializedException,SQLException{
		String sql  = "select a.*,b.* from td_topic a, td_user b where a.userid=b.id and a.id=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, id);
				rs = ps.executeQuery();
				try {
					if (rs.next()) {
						TopicListEleEntity e = new TopicListEleEntity();
						TopicEntity topic = new TopicEntity();
						UserEntity user = new com.jw.remote.entity.UserEntity();
						
						topic.setId(rs.getLong("a.id"));
						topic.setTopic(rs.getString("topic"));
						topic.setEmotion(rs.getString("emotion"));
						topic.setCreated_at(rs.getTimestamp("created_at"));
						topic.setUpdated_at(rs.getTimestamp("updated_at"));
						topic.setUserid(rs.getLong("userid"));
						topic.setContent(rs.getString("content"));
						topic.setCheck(rs.getInt("checks"));
						topic.setOriginal(rs.getInt("original"));
						
						user.setId(rs.getLong("b.id"));
						user.setPhone(rs.getString("phone"));
						user.setSex(rs.getInt("sex"));
						user.setUsername(rs.getString("username"));
						user.setWeixin(rs.getString("weixin"));
						user.setLimits(rs.getInt("limits"));
						user.setProv(rs.getInt("prov"));
						user.setCity(rs.getInt("city"));
 
						e.setTopic(topic);
						e.setUser(user);
						return e;
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
	 * 删除内容 topic
	 * @param id
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void deleteTopic(long id)throws TimeoutException,NotInitializedException,SQLException{
		String sql = "delete from td_topic where id=?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setLong(1, id);
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
	 * 新增主题内容
	 * @param topic
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void addTopic(TopicEntity topic) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "insert into td_topic(topic, emotion, created_at, updated_at, userid,content,checks,original) values(?,?,?,?,?,?,?,?)";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setString(1, topic.getTopic());
				ps.setString(2, topic.getEmotion());
				ps.setTimestamp(3, new Timestamp(topic.getCreated_at().getTime()));
				ps.setTimestamp(4, new Timestamp(topic.getUpdated_at().getTime()));
				ps.setLong(5, topic.getUserid());
				ps.setString(6, topic.getContent());
				ps.setInt(7, topic.getCheck());
				ps.setInt(8, topic.getOriginal());
				ps.execute();
				
			} finally {
				ps.close();
			}
			long id = queryLastInsertId(conn);
			topic.setId(id);
		} catch (SQLException e) {
			conn.close();
			throw e;
		} finally {
			pool.release(conn);
		}
	}
	
	
	/**
	 * 
	 * @param index
	 * @param size
	 * @return
	 * @throws SQLException 
	 * @throws NotInitializedException 
	 * @throws TimeoutException 
	 */
	public List<TopicListEleEntity> queryTopicsByPage(int index, int size, String phone) throws SQLException, TimeoutException, NotInitializedException{
		List<TopicListEleEntity> list = new ArrayList<TopicListEleEntity>();
		String sql;
		int start = index*size;
		sql = "select a.*,b.* from td_topic a, td_user b where a.userid=b.id and b.phone=?  order by a.created_at desc limit ?,?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setString(1, phone);
				ps.setLong(2, start);
				ps.setLong(3, size);
				rs = ps.executeQuery();
				try {
					while (rs.next()) {
						TopicListEleEntity e = new TopicListEleEntity();
						TopicEntity topic = new TopicEntity();
						UserEntity user = new com.jw.remote.entity.UserEntity();
						
						topic.setId(rs.getLong("a.id"));
						topic.setTopic(rs.getString("topic"));
						topic.setEmotion(rs.getString("emotion"));
						topic.setCreated_at(rs.getTimestamp("created_at"));
						topic.setUpdated_at(rs.getTimestamp("updated_at"));
						topic.setUserid(rs.getLong("userid"));
						topic.setContent(rs.getString("content"));
						topic.setCheck(rs.getInt("checks"));
						topic.setOriginal(rs.getInt("original"));
						
						user.setId(rs.getLong("b.id"));
						user.setPhone(rs.getString("phone"));
						user.setSex(rs.getInt("sex"));
						user.setUsername(rs.getString("username"));
						user.setWeixin(rs.getString("weixin"));
						user.setLimits(rs.getInt("limits"));
						user.setZone(rs.getString("zone"));
						user.setSign(rs.getString("sign"));
						user.setProv(rs.getInt("prov"));
						user.setCity(rs.getInt("city"));
						
						e.setTopic(topic);
						e.setUser(user);
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
	 * 
	 * @param index
	 * @param size
	 * @return
	 * @throws SQLException 
	 * @throws NotInitializedException 
	 * @throws TimeoutException 
	 */
	public List<TopicListEleEntity> queryTopicsByPage(int index, int size,int checks) throws SQLException, TimeoutException, NotInitializedException{
		List<TopicListEleEntity> list = new ArrayList<TopicListEleEntity>();
		String sql;
		int start = index*size;
		sql = "select a.*,b.* from td_topic a, td_user b where a.userid=b.id and a.checks=?  order by a.created_at desc limit ?,?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setInt(1, checks);
				ps.setLong(2, start);
				ps.setLong(3, size);
				rs = ps.executeQuery();
				try {
					while (rs.next()) {
						TopicListEleEntity e = new TopicListEleEntity();
						TopicEntity topic = new TopicEntity();
						UserEntity user = new com.jw.remote.entity.UserEntity();
						
						topic.setId(rs.getLong("a.id"));
						topic.setTopic(rs.getString("topic"));
						topic.setEmotion(rs.getString("emotion"));
						topic.setCreated_at(rs.getTimestamp("created_at"));
						topic.setUpdated_at(rs.getTimestamp("updated_at"));
						topic.setUserid(rs.getLong("userid"));
						topic.setContent(rs.getString("content"));
						topic.setCheck(rs.getInt("checks"));
						topic.setOriginal(rs.getInt("original"));
						
						user.setId(rs.getLong("b.id"));
						user.setPhone(rs.getString("phone"));
						user.setSex(rs.getInt("sex"));
						user.setUsername(rs.getString("username"));
						user.setWeixin(rs.getString("weixin"));
						user.setLimits(rs.getInt("limits"));
						user.setZone(rs.getString("zone"));
						user.setSign(rs.getString("sign"));
						user.setProv(rs.getInt("prov"));
						user.setCity(rs.getInt("city"));
 
						e.setTopic(topic);
						e.setUser(user);
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
}
