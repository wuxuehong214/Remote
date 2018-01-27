package com.jw.remote.resource;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jw.remote.entity.TopicEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.ResultEntity;
import com.jw.remote.entity.ext.TopicListEleEntity;
import com.jw.remote.service.TopicService;
import com.jw.remote.service.UserService;

/**
 * 
 * @author Administrator
 * 
 */
@Path("topic")
public class TopicResource {

	private Logger logger = Logger.getLogger(TopicResource.class);
	private TopicService topicService = new TopicService();
	private UserService userService = new UserService();
	
	@GET
	@Path("get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String queryTopic(@PathParam("id") long id){
		logger.info("请求查询内容详情:"+id);
		ResultEntity<TopicListEleEntity> r = topicService.queryTopic(id);
		return JSONObject.toJSONString(r);
	}

	@DELETE
	@Path("delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTopic(@PathParam("id") long id) {
		logger.info("请求删除内容信息:" + id);
		ResultEntity<TopicListEleEntity> r = topicService.deleteTopic(id);
		return JSONObject.toJSONString(r);
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public String queryTopicList(
			@DefaultValue("0") @QueryParam("index") int index,
			@DefaultValue("10") @QueryParam("size") int size,
			@DefaultValue("1") @QueryParam("check") int check) {
		logger.info("请求查询内容列表！index:" + index + "\tsize:" + size+"\tcheck:"+check);
		List<TopicListEleEntity> list = topicService.queryTopics(index, size,check);
		return JSONArray.toJSONString(list);
	}
	
	@GET
	@Path("list/{phone}")
	@Produces(MediaType.APPLICATION_JSON)
	public String queryTopicList(
			@PathParam("phone")String phone,
			@DefaultValue("0") @QueryParam("index") int index,
			@DefaultValue("10") @QueryParam("size") int size
			) {
		logger.info("请求查询内容列表！index:" + index + "\tsize:" + size+"\tphone:"+phone);
		List<TopicListEleEntity> list = topicService.queryTopics(index, size,phone);
		return JSONArray.toJSONString(list);
	}
	
	@POST
	@Path("tipoff/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String tipOff(@PathParam("id")long id,String entity){
		
		JSONObject obj = JSONObject.parseObject(entity);
		String phone = (String)obj.get("phone");
		logger.info("请求举报:"+id+"\t举报人:"+phone);
		
		ResultEntity<String> r = new ResultEntity<String>();
		return JSONObject.toJSONString(r);
	}

	@POST
	@Path("publish")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String publishTopic(String entity) {
		logger.info("请求发布内容:" + entity);
		
		TopicEntity topic;
		ResultEntity<TopicEntity> result = new ResultEntity<TopicEntity>();
		try {
			topic = JSONObject.parseObject(entity, TopicEntity.class);
		} catch (Exception e) {
			logger.warn("发布内容请求信息json解析异常:"+e.getMessage());
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("发布内容请求信息json解析异常:"+e.getMessage());
			return JSONObject.toJSONString(result);
		}
		result.setData(topic);
		
		//验证用户身份
		String phone = topic.getPhone();
		UserEntity user = userService.queryUser(phone);
		if(user == null){
			logger.info("当前发布消息身份["+phone+"]无效!请确认注册并登陆");
			result.setReason("当前发布消息身份["+phone+"]无效!请确认注册并登陆");
			result.setResult(ResultEntity.RESULT_FAIL);
			return JSONObject.toJSONString(result);
		}
		
		//如果是黑名单
		if(user.getLimits() == UserEntity.LIMITS_BLACK){
			logger.info("当前账号["+phone+"]已加入黑名单，无法发布!");
			result.setResult(ResultEntity.RESULT_FAIL);
			return JSONObject.toJSONString(result);
		}
		
		//如果是白名单 则topic不需要审核
		//如果是普通用户 则topic状态为待审核
		if(user.getLimits() == UserEntity.LIMITS_NORMAL)
			    topic.setCheck(TopicEntity.CHECK_WAIT);
		else if(user.getLimits() == UserEntity.LIMITS_WHITE)
			  topic.setCheck(TopicEntity.CHECK_SUCCESS);
		else{
			result.setReason("当前用户["+phone+"]权限未知!");
			result.setResult(ResultEntity.RESULT_FAIL);
			return JSONObject.toJSONString(result);
		}
		
		topic.setUserid(user.getId());
		topic.setCreated_at(new Date());
		topic.setUpdated_at(new Date());
		
		result = topicService.publishTopic(topic);
		return JSONObject.toJSONString(result);
	}

}
