package com.jw.remote.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.TimeoutException;

import com.jw.remote.db.DatabasePool;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.SuggestEntity;

/**
 * 意见信息实体 数据库操作对象
 * @author Administrator
 *
 */
public class SuggestDao {
	
	private DatabasePool pool = DatabasePool.getInstance();
	
	/**
	 * 新增意见
	 * @param suggest
	 * @throws TimeoutException
	 * @throws NotInitializedException
	 * @throws SQLException
	 */
	public void addSuggest(SuggestEntity suggest) throws TimeoutException, NotInitializedException, SQLException{
		String sql = "insert into td_suggest(sjc,content,phone) values(?,?,?)";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setTimestamp(1, new Timestamp(suggest.getSjc().getTime()));
				ps.setString(2, suggest.getContent());
				ps.setString(3, suggest.getPhone());
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
	

}
