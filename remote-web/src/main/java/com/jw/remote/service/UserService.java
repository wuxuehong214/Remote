package com.jw.remote.service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.jw.remote.dao.UserDao;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.UserStasticEntity;
import com.jw.remote.entity.ext.ResultEntity;

public class UserService {
	
	private Logger logger = Logger.getLogger(UserService.class);
	private UserDao userDao = new UserDao();
	
	
	public ResultEntity<UserEntity> login(UserEntity user){
		ResultEntity<UserEntity> result = new ResultEntity<UserEntity>();
		
		try {
			UserEntity u = userDao.getUser(user.getPhone());
			if(u == null){
				result.setResult(2);
				result.setReason("账号不存在!");
				return result;
			}
			if(u.getPassword()!=null && !u.getPassword().equals(user.getPassword())){
				result.setResult(3);
				result.setReason("密码错误");
				return result;
			}
			result.setResult(1);
			result.setData(u);
			u.setPassword(null);
//			u.setTouxiang(null);
			return result;
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("查询用户信息时数据库操作异常!",e);
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("查询用户信息时数据库操作异常");
			return result;
		}
	}
	/**
	 * 获取省份城市下用户列表
	 * @return
	 */
	public ResultEntity<List<UserEntity>> listByProvCity(int prov, int city,int index, int size){
		ResultEntity<List<UserEntity>> r = new ResultEntity<List<UserEntity>>();
		try {
			List<UserEntity> list = userDao.getUserListByProvCity(prov, city, index ,size);
			r.setResult(ResultEntity.RESULT_SUCCESS);
			r.setData(list);
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("按省份城市获取用户列表时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("按省份城市获取用户列表时数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
		}
		return r;
	}

	/**
	 * 按照省份统计
	 * @return
	 */
	public ResultEntity<List<UserStasticEntity>> countByProv(){
		ResultEntity<List<UserStasticEntity>> r = new ResultEntity<List<UserStasticEntity>>();
		try {
			List<UserStasticEntity> list = userDao.getUserCountByProv();
			r.setData(list);
			r.setResult(ResultEntity.RESULT_SUCCESS);
			return r;
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("按省份统计 用户数时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("按省份统计 用户数时数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		
	}
	
	/**
	 * 按指定省份统计 城市下
	 * @return
	 */
	public ResultEntity<List<UserStasticEntity>> countByProvCity(int prov){
		ResultEntity<List<UserStasticEntity>> r = new ResultEntity<List<UserStasticEntity>>();
		try {
			List<UserStasticEntity> list = userDao.getUserCountByProvCity(prov);
			r.setData(list);
			r.setResult(ResultEntity.RESULT_SUCCESS);
			return r;
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("按省份统计 用户数时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("按省份统计 用户数时数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		
	}
	
	/**
	 * 按指定省份统计 城市下
	 * @return
	 */
	public ResultEntity<List<UserStasticEntity>> countByProvCity(int prov,int city){
		ResultEntity<List<UserStasticEntity>> r = new ResultEntity<List<UserStasticEntity>>();
		try {
			List<UserStasticEntity> list = userDao.getUserCountByProvCity(prov,city);
			r.setData(list);
			r.setResult(ResultEntity.RESULT_SUCCESS);
			return r;
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("按省份城市统计 用户数时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("按省份城市统计 用户数时数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		
	}
	
	
 	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public ResultEntity<UserEntity> updateUser(UserEntity user){
		ResultEntity<UserEntity> r = new ResultEntity<UserEntity>();
		
		if(user == null || "".equals(user.getPhone()) || user.getPhone() == null){
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("用户实体信息不明!");
			r.setData(user);
			return r;
		}
		
		try {
			userDao.updateUser(user);
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("更新用户信息时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("更新用户信息时数据库操作异常:"+e.getMessage());
			r.setData(user);
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		
		r.setData(user);
		r.setResult(ResultEntity.RESULT_SUCCESS);
		return r;
	}
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public ResultEntity<UserEntity> registerUser(UserEntity user){
		ResultEntity<UserEntity> r = new ResultEntity<UserEntity>();
		r.setData(user);
		r.setResult(ResultEntity.RESULT_SUCCESS);
		try {
			userDao.addUser(user);
		} catch (Exception e) {
			logger.warn("注册用户信息时数据库操作异常:"+e.getMessage());
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("注册用户信息时数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		return r;
	}
	
	/**
	 * 删除用户
	 * @param phone
	 * @return
	 */
	public ResultEntity<UserEntity> deleteUser(String phone){
		
		ResultEntity<UserEntity> result = new ResultEntity<UserEntity>();
		UserEntity u = new UserEntity();
		u.setPhone(phone);
		result.setData(u);
		try {
			userDao.deleteUser(phone);
		} catch (Exception e) {
			logger.warn("更新用户信息时数据库操作异常:"+e.getMessage());
			result.setResult(0);
			result.setReason("更新用户信息时数据库操作异常:"+e.getMessage());
			result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return result;
		}
		result.setResult(ResultEntity.RESULT_SUCCESS);
		return result;
	}
	
	/**
	 * 根据手机号查询
	 * @param userid
	 * @return
	 */
	public UserEntity queryUser(String phone){
		try {
			return userDao.getUser(phone);
		} catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询用户信息 基本 ，不带密码
	 * @param phone
	 * @return
	 */
	public ResultEntity<UserEntity> queryUserBasicInfo(String phone){
		ResultEntity<UserEntity> result = new ResultEntity<UserEntity>();
		
		try {
			UserEntity user = userDao.getUser(phone);
			if(user == null) {
				result.setResult(ResultEntity.RESULT_FAIL);
				result.setReason("用户不存在!");
				return result;
			}
			user.setPassword(null);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			result.setData(user);
			return result;
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("查询用户信息时数据库操作异常!",e);
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("查询用户信息时数据库操作异常!");
			result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return result;
		}
		
	}
//
//	/**
//	 * 查询朋友
//	 * @param userid
//	 * @return
//	 */
//	public List<UserEntity> queryFriends(int userid){
//		try {
//			return userDao.queryFriendUsers(userid);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//		}
//		return new ArrayList<UserEntity>();
//	}
}
