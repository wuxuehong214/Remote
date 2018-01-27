package com.jw.remote.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jw.remote.entity.GuanZhuEntity;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.ext.GuanZhuListEleEntity;
import com.jw.remote.entity.ext.ResultEntity;
import com.jw.remote.service.GuanZhuService;
import com.jw.remote.service.UserService;

@Path("gz")
public class GuanZhuResource {
	
	private Logger logger = Logger.getLogger(GuanZhuResource.class);
	private UserService userService = new UserService();
	private GuanZhuService gzService = new GuanZhuService();
	
	/**
	 * 
	 * @param phone:   请求者 手机号
	 * @param userid： 要关注的人的ID
	 * @return
	 */
	@GET
	@Path("req/{phone}")
	public String requestGuanZhu(@PathParam("phone")String phone,@QueryParam("userid")String userid){
		
		logger.info("请求关注，请求方:"+phone+"\t被关注方:"+userid);
		
		UserEntity user = userService.queryUser(phone);
		ResultEntity<GuanZhuEntity> result = new ResultEntity<GuanZhuEntity>();
		if(user == null){
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("当前用户未注册");
			return JSONObject.toJSONString(result);
		}
		
		if(userid == null || "".equals(userid)){
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("关注方ID异常:"+userid);
			return JSONObject.toJSONString(result);
		}
		
		GuanZhuEntity gz = new GuanZhuEntity();
		gz.setUserid(user.getId());
		gz.setGzuserid(Long.parseLong(userid));
		
		result = gzService.insertGuanZhu(gz);
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@Path("list/{phone}")
	public String queryGuanZhuList(@PathParam("phone")String phone){
		logger.info("请求查询关注人列表:"+phone);
		
		UserEntity user = userService.queryUser(phone);
		ResultEntity<GuanZhuEntity> result = new ResultEntity<GuanZhuEntity>();
		if(user == null){
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("当前用户未注册!");
			return JSONObject.toJSONString(result);
		}
		
		ResultEntity<List<GuanZhuListEleEntity>> r = gzService.queryGuanzhu(user.getId());
		return JSONObject.toJSONString(r);
	}
	
	@DELETE
	@Path("cancel/{gzid}")
	public String cancelGuanZhu(@PathParam("gzid")String gzid){
		logger.info("请求取消关注:"+gzid);
		
		ResultEntity<GuanZhuEntity> result = new ResultEntity<GuanZhuEntity>();
		long id;
		try{
		  id = Long.parseLong(gzid);
		}catch(Exception e){
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("未知关注ID:"+gzid);
			return JSONObject.toJSONString(result);
		}
		
		ResultEntity<GuanZhuEntity> r = gzService.removeGuanZhu(id);
		return JSONObject.toJSONString(r);
	}
		
		

}
