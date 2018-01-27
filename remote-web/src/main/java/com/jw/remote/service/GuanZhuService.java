package com.jw.remote.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jw.remote.dao.GuanZhuDao;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.GuanZhuEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.GuanZhuListEleEntity;
import com.jw.remote.entity.ext.ResultEntity;

public class GuanZhuService {
	
	private Logger logger = Logger.getLogger(GuanZhuService.class);
	private GuanZhuDao dao = new GuanZhuDao();
	
	public static void main(String args[]){
		 ResultEntity<List<GuanZhuListEleEntity>> result = new  ResultEntity<List<GuanZhuListEleEntity>>();
		 List<GuanZhuListEleEntity> list = new ArrayList<GuanZhuListEleEntity>();
		 
		 GuanZhuListEleEntity g1 = new GuanZhuListEleEntity();
		 g1.setId(1);
		 UserEntity user = new UserEntity();
		 g1.setUser(user);
		 user.setId(78);
		 user.setPhone("15802540365");
		 user.setSex(1);
		 user.setUsername("莫问今朝");
		 user.setWeixin("iever");
		 list.add(g1);
		 
		 GuanZhuListEleEntity g2 = new GuanZhuListEleEntity();
		 g1.setId(2);
		 UserEntity user2 = new UserEntity();
		 g2.setUser(user2);
		 user2.setId(79);
		 user2.setPhone("18868238126");
		 user2.setSex(1);
		 user2.setUsername("司马一冰");
		 user2.setWeixin("呵呵");
		 list.add(g2);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			result.setData(list);
			System.out.println(JSONObject.toJSON(result));
	}

	/**
	 * 取消关注
	 * @param entity
	 * @return
	 */
	public ResultEntity<GuanZhuEntity> removeGuanZhu(long gzid){
		ResultEntity<GuanZhuEntity> result = new ResultEntity<GuanZhuEntity>();
		
		try {
			dao.delGuanZhu(gzid);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			return result;
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("取消关注时数据库操作异常!",e);
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("取消关注时数据库操作异常!");
			result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return result;
		}
	}
	
	/**
	 * 查询关注列表
	 * @param userid
	 * @return
	 */
	public ResultEntity<List<GuanZhuListEleEntity>> queryGuanzhu(long userid){
		 ResultEntity<List<GuanZhuListEleEntity>> result = new  ResultEntity<List<GuanZhuListEleEntity>>();
		 try {
			List<GuanZhuListEleEntity> list = dao.getGuanZhus(userid);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			result.setData(list);
			return result;
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("查询关注人列表时数据库操作异常!",e);
			   result.setResult(ResultEntity.RESULT_FAIL);
			   result.setReason("查询关注人列表时数据库操作异常!["+e.getMessage()+"]");
			   result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			   return result;
		}
	}
	/**
	 * 新增关注
	 * @param entity
	 * @return
	 */
	public ResultEntity<GuanZhuEntity> insertGuanZhu(GuanZhuEntity entity){
		
		ResultEntity<GuanZhuEntity> result = new ResultEntity<GuanZhuEntity>();
		
		try{
			GuanZhuEntity gz = dao.getGuanZhu(entity.getUserid(), entity.getGzuserid());
			if(gz != null){
				   result.setResult(ResultEntity.RESULT_FAIL);
				   result.setReason("该用户已关注过");
				   result.setData(gz);
				  return result;
			}
			
			dao.addGuanZhu(entity);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			result.setData(entity);
			return result;
		}catch(Exception e){
				logger.warn("请求关注时，数据库操作异常",e);
			   result.setResult(ResultEntity.RESULT_FAIL);
			   result.setReason("请求关注时，数据库操作异常：["+e.getMessage()+"]");
			   result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			   result.setData(entity);
			   return result;
		}
	}

}
