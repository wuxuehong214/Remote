package com.jw.remote.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author wuxuehong
 * @project c2-cloud-monit-backend
 * @date 2014-11-26
 * @company china creator
 */
public class DBFactory {
	//
	//c2cloud-v2   username:c2cloud-v2-dev
	//c2cloud-test username:root
	
	//最新mysql
	//c2cloud_v2  root  mysql  172.16.71.45
	private static final String url = "jdbc:mysql://127.0.0.1:3306/remote?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String username = "root";
	private static final String password = "wuxuehong";

	private static Connection conn = null;
	
	public static void main(String args[]){
		getConnection();
	}

	public static Connection getConnection() {
		
		try {
			if(conn != null && !conn.isClosed())
				return conn;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}// 指定连接类型
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("connected");
		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			conn = null;
			se.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeResultSet(ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement ps){
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
