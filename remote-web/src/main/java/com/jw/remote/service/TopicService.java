package com.jw.remote.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.jw.remote.dao.TopicDao;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.TopicEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.ResultEntity;
import com.jw.remote.entity.ext.TopicListEleEntity;

public class TopicService {

	private Logger logger = Logger.getLogger(TopicService.class);
	private TopicDao dao = new TopicDao();
	
	/**
	 * 查询内容
	 * @param id
	 * @return
	 */
	public ResultEntity<TopicListEleEntity>  queryTopic(long id){
		
		TopicListEleEntity t = null;
		ResultEntity<TopicListEleEntity> r = new ResultEntity<TopicListEleEntity>();
		try {
			t = dao.getTopic(id);
			r.setData(t);
			r.setResult(ResultEntity.RESULT_SUCCESS);
			return r;
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("queryTopic数据库操作异常!"+e.getMessage());
			r.setData(null);
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("查询过程中数据库操作异常:"+e.getMessage());
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
	}
	
	/**
	 * 删除topic
	 * @param id
	 * @return
	 */
	public ResultEntity<TopicListEleEntity> deleteTopic(long id){
		ResultEntity<TopicListEleEntity> r = new ResultEntity<TopicListEleEntity>();
		TopicListEleEntity t = null;
		try {
			t = dao.getTopic(id);
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("数据库操作(查询)异常!");
			r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
			return r;
		}
		
		if(t != null){
			 try {
				dao.deleteTopic(id);
			} catch (TimeoutException | NotInitializedException | SQLException e) {
				r.setResult(ResultEntity.RESULT_FAIL);
				r.setReason("数据库操作(删除)异常!");
				r.setCode(ResultEntity.CODE_SQL_EXCEPTION);
				return r;
			}
			 r.setResult(ResultEntity.RESULT_SUCCESS);
			 r.setData(t);
			 return r;
		}else{
			r.setResult(ResultEntity.RESULT_FAIL);
			r.setReason("删除的对象不存在!");
			return r;
		}
	}
	
	/**
	 * 发布
	 * @param topic
	 * @return
	 */
	public ResultEntity<TopicEntity> publishTopic(TopicEntity topic){
		ResultEntity<TopicEntity> r = new ResultEntity<TopicEntity>();
		r.setData(topic);
		r.setResult(ResultEntity.RESULT_SUCCESS);
		try {
			dao.addTopic(topic);
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("发布内容异常:"+e.getMessage());
			r.setResult(0);
			r.setReason("发布topic时数据库操作异常:"+e.getMessage());
		}
		return r;
	}
	
	/**
	 * 获取内容列表
	 * @param index
	 * @param size
	 * @return
	 */
	public List<TopicListEleEntity> queryTopics(int index, int size,int checks){
		try {
			return dao.queryTopicsByPage(index, size,checks);
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("获取内容列表异常:"+e.getMessage());
		}
		return new ArrayList<TopicListEleEntity>();
	}
	
	public List<TopicListEleEntity> queryTopics(int index, int size,String phone){
		try {
			return dao.queryTopicsByPage(index, size,phone);
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("获取内容列表异常:"+e.getMessage());
		}
		return new ArrayList<TopicListEleEntity>();
	}
	
	public static void main(String args[]){
		List<TopicListEleEntity> list = new ArrayList<TopicListEleEntity>();
		
		TopicListEleEntity t1 = new TopicListEleEntity();
		t1.setUser(new UserEntity());
		t1.setTopic(new TopicEntity());
		t1.getUser().setUsername("mowenjinzhao");
		t1.getTopic().setContent("ddddd");
		
		TopicListEleEntity t2 = new TopicListEleEntity();
		t2.setUser(new UserEntity());
		t2.setTopic(new TopicEntity());
		t2.getUser().setUsername("mowenjinzhao");
		t2.getTopic().setContent("ddddd");
		list.add(t1);
		list.add(t2);
		
		System.out.println(JSONArray.toJSONString(list));
	}
	
}
