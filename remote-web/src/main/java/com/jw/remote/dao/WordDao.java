package com.jw.remote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.jw.remote.db.DatabasePool;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.WordEntity;

public class WordDao {

	private DatabasePool pool = DatabasePool.getInstance();

	/**
	 * 获取随机分词
	 * @param size
	 * @return
	 * @throws SQLException 
	 * @throws NotInitializedException 
	 * @throws TimeoutException 
	 */
	public List<WordEntity> getRandomWords(int size) throws SQLException, TimeoutException, NotInitializedException {
		List<WordEntity> list = new ArrayList<WordEntity>();
		String sql = "select * from td_word where id >= (select(floor(RAND()*(SELECT MAX(id) from td_word)))) order by id limit ?";
		Connection conn = pool.getConnection();
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			try {
				ps.setInt(1, size);
				rs = ps.executeQuery();
				try {
					while (rs.next()) {
						WordEntity w = new WordEntity();
						w.setId(rs.getLong("id"));
						w.setWord(rs.getString("word"));
						list.add(w);
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
