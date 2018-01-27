package com.jw.remote.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class DatabasePool {

	private Logger logger = Logger.getLogger(DatabasePool.class);
	private String url;
//	private final String P_URL = "url";
	private String driver;
//	private final String P_DRIVER = "driver";
	private String username;
//	private final String P_USERNAME = "username";
	private String password;
//	private final String P_PASSWORD = "password";
	private int initialConnections = 1;
//	private final String P_INIT_CONNECTIONS = "initConnections";
	private int incrementalConnections = 1;
//	private final String P_INCREMENT_CONNECTIONS = "incrementConnections";
	private int maxConnections = 50;
//	private final String P_MAX_CONNECTIONS = "maxConnections";
//	private static volatile DatabasePool instance = null;
	private boolean init = false;
	private Vector<DBConnection> connections = null;

	private static class DatabasePoolHandler{
		private static DatabasePool instance = new DatabasePool();
	}
	
	public static DatabasePool getInstance() {
		return DatabasePoolHandler.instance;
	}

	private DatabasePool() {
		connections = new Vector<DBConnection>();
		try {
			init(DatabaseConfiguration.initConfiguration());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (BeenInitializedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void init(DatabaseConfiguration config) throws BeenInitializedException,
			ClassNotFoundException, SQLException {
		if (init)
			throw new BeenInitializedException(
					"Database connection pool has been initialized!");
		if (config == null)
			throw new NullPointerException("DatabaseConfiguration is null!");
		this.url = config.getUrl();
		this.driver = config.getDriver();
		this.username = config.getUsername();
		this.password = config.getPassword();
		this.initialConnections = config.getInitConnections();
		this.incrementalConnections = config.getIncrementBy();
		this.maxConnections = config.getMaxConnections();

		logger.info("Database connection pool parameters:");
		logger.info("url:" + url);
		logger.info("driver:" + driver);
		logger.info("username:" + username);
		logger.info("password:" + password);
		logger.info("initialConnections:" + initialConnections);
		logger.info("incrementalConnections:" + incrementalConnections);
		logger.info("maxConnections:" + maxConnections);

		try {
			logger.debug("Load sql driver:" + driver);
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		}

		logger.debug("Create initialed connections:" + initialConnections);
		init = true;
		
		int r = createConnections(initialConnections);
		
		logger.info("Init connections:"+r);
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				while(true){
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					System.out.println("当前可用连接数："+getIdledConnectionCounts());
//				}
//			}
//		}).start();
	}

	public synchronized void releaseRandom() {
		Random r = new Random();
		int index = r.nextInt(connections.size());
		release(connections.get(index).getConnection());
		this.notifyAll();
	}

	/**
	 * 关闭连接池
	 */
	public synchronized void shutDown() {
		for (int i = 0; i < connections.size();) {
			DBConnection dbConn = connections.remove(i);
			try {
				dbConn.getConnection().close();
				logger.info("Close connection:" + dbConn.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放连接
	 * 
	 * @param conn
	 */
	public synchronized void release(Connection conn) {
		if (conn == null)
			return;
		for (int i = 0; i < connections.size(); ) {
			DBConnection dbCon = connections.get(i);
			if (dbCon.getConnection() == conn) {
				boolean is = false;
				try {
					is = conn.isClosed();
				} catch (SQLException e) {
					try {
						conn.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					is = true;
				}
				if (!is) {
					dbCon.setState(0);
					logger.debug("A connection is released!");
					i++;
				}else{
					connections.remove(i);
					logger.debug("Remove a closed connection!");
				}
			}else{
				i++;
			}
		}
		//居然忘记了唤醒！！！！！！！！
		notifyAll();
	}

	public synchronized Connection getConnection() throws TimeoutException, NotInitializedException {
		return getConnection(1000);
	}

	Set<Thread> ts = new HashSet<Thread>();

	/**
	 * 从连接池获取连接 如果没有可用连接将等待5s 5s后还没有连接的话则抛出超时异常
	 * 
	 * @return
	 * @throws TimeoutException
	 * @throws NotInitializedException 
	 */
	public synchronized Connection getConnection(long time_out_seconds) throws TimeoutException, NotInitializedException
			{
		if(!init) throw new NotInitializedException("线程池尚未初始化!");
		logger.debug("\n\nCall to get a connection from pool!"+time_out_seconds);
		Connection conn = getIdledConnection();
		if (conn == null) { 
			// 扩展连接
			try {
				logger.debug("Non ideld connections, create more connections..");
				int count = createConnections(incrementalConnections);
				logger.debug("Successfully create connections:" + count);
				if (count > 0)
					this.notifyAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
//			int count = 0;
			long last = System.currentTimeMillis();
			long delta = 0;
			while ((conn = getIdledConnection()) == null) {
				try {
					delta = (time_out_seconds*1000)-(System.currentTimeMillis()-last);
					if(delta <= 0){
						this.notifyAll();
						throw new TimeoutException();
					}
					this.wait(delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.debug("Last get connection:" + conn);
			return conn;
		} else {
			logger.debug("First get idled connection:" + conn);
			return conn;
		}
	}

	/**
	 * 获取线程池中空闲的连接
	 * 
	 * @return
	 */
	private Connection getIdledConnection() {
		for (int i = 0; i < connections.size(); i++) {
			DBConnection conn = connections.get(i);
			if (conn.state == 0) {
				conn.setState(1);
				return conn.getConnection();
			}
		}
		return null;
	}
	
	public int getIdledConnectionCounts(){
		int count = 0;
		for (int i = 0; i < connections.size(); i++) {
			DBConnection conn = connections.get(i);
			if (conn.state == 0) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 创建新连接
	 * 
	 * @param num
	 * @throws SQLException
	 * @throws NotInitializedException 
	 */
	private int createConnections(int num) throws SQLException {
		int count = 0;
		for (int i = 0; i < num; i++) {
			if (connections.size() < maxConnections)
				try {
					Connection conn = DriverManager.getConnection(url,
							username, password);
					logger.error("Connected....");
					connections.add(new DBConnection(conn));
					count++;
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
		}
		return count;
	}

	static class DBConnection {
		Connection connection;
		long lastTime;
		int state; // 1-working 0-idel

		public DBConnection(Connection conn) {
			connection = conn;
			lastTime = System.currentTimeMillis();
			state = 0;
		}

		public Connection getConnection() {
			return connection;
		}

		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		public long getLastTime() {
			return lastTime;
		}

		public void setLastTime(long lastTime) {
			this.lastTime = lastTime;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
	}
}
