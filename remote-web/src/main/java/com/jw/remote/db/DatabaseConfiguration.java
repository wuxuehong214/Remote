package com.jw.remote.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * 数据库配置
 * 
 * @author wuxuehong
 * @project monit-alarm
 * @date 2015-6-8
 * @company china creator
 */
public class DatabaseConfiguration {
	
	private static Logger logger = Logger.getLogger(DatabaseConfiguration.class);

	public static final String URL = "url";
	public static final String DRIVIER = "driver";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String INIT_CONNECTIONS = "initConnections";
	public static final String INCREAMENT_BY = "incrementBy";
	public static final String MAXCONNETIONS = "maxConnections";

	private String url; // 数据库访问URL
	private String driver; // 数据库驱动类
	private String username; // 数据库访问用户名
	private String password; // 数据库访问密码
	private int initConnections = 2; // 初始化连接数
	private int incrementBy = 1; // 每次新增连接时 增加的连接数
	private int maxConnections = 10; // 连接池中最多连接数

	private DatabaseConfiguration() {
	}

	public static DatabaseConfiguration initConfiguration() {
		Properties prop = new Properties();
		File f = new File("conf/sql.properties");
		if (f.exists()) {
			try {
				InputStream is = new FileInputStream(f);
				prop.load(is);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} else{
			try {
				InputStream is = DatabaseConfiguration.class.getClassLoader()
						.getResourceAsStream("sql.properties");
				prop.load(is);
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}
		
		DatabaseConfiguration config = new DatabaseConfiguration();
		config.setUrl(prop.getProperty(URL));
		config.setDriver(prop.getProperty(DRIVIER));
		config.setUsername(prop.getProperty(USERNAME));
		config.setPassword(prop.getProperty(PASSWORD));
		config.setInitConnections(Integer.parseInt(prop.getProperty(INIT_CONNECTIONS,"2")));
		config.setIncrementBy(Integer.parseInt(prop.getProperty(INCREAMENT_BY, "1")));
		config.setMaxConnections(Integer.parseInt(prop.getProperty(MAXCONNETIONS, "10")));
		return config;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitConnections() {
		return initConnections;
	}

	public void setInitConnections(int initConnections) {
		this.initConnections = initConnections;
	}

	public int getIncrementBy() {
		return incrementBy;
	}

	public void setIncrementBy(int incrementBy) {
		this.incrementBy = incrementBy;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

}
