package com.jw.remote.resource;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jw.remote.entity.UserEntity;
import com.jw.remote.entity.UserStasticEntity;
import com.jw.remote.entity.ext.ResultEntity;
import com.jw.remote.service.UserService;

/**
 * 
 * 
 * @author Administrator
 * 
 */

@Path("user")
public class UserResource {

	private Logger logger = Logger.getLogger(UserResource.class);
	private UserService userService = new UserService();

	@Context
	private HttpServletRequest request;

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateUser(String entity) {
		logger.info("请求更新用户:" + entity);
		UserEntity user = null;
		ResultEntity<UserEntity> result = new ResultEntity<UserEntity>();
		
		try {
			user = JSONObject.parseObject(entity, UserEntity.class);
		} catch (Exception e) {
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("请求内容，使用json解析异常:"+entity);
			return JSONObject.toJSONString(result);
		}
		
		if (user != null && user.getTouxiang() != null &&!"".equals(user.getTouxiang())) {
			logger.info("请求更新用户头像:"+user.getPhone());
			String savePath = request.getSession().getServletContext()
					.getRealPath("/tx");
			File f = new File(savePath);
			if (!f.exists())
				f.mkdirs();

			String scalePath = request.getSession().getServletContext()
					.getRealPath("/tx/tiny");
			File f2 = new File(scalePath);
			if (!f2.exists())
				f2.mkdirs();

			String image = user.getTouxiang();
			int index = image.indexOf(',');
			if (index != -1) {
				image = image.substring(index + 1);
			}

			byte[] buffer = Base64.decodeBase64(image);
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			try {

				// 原始图片保存
				BufferedImage bi = ImageIO.read(bis);
				File ff = new File(savePath + "/" + user.getPhone());
				ImageIO.write(bi, "png", ff);

				// 压缩图片保存 缩略图
				File ff2 = new File(scalePath + "/" + user.getPhone());
				Image src = bi.getScaledInstance(48, 48, Image.SCALE_DEFAULT);
				BufferedImage dest = new BufferedImage(48, 48,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = dest.getGraphics();
				g.drawImage(src, 0, 0, null);
				g.dispose();
				ImageIO.write(dest, "png", ff2);
				
				//压缩图片保存缩略图 100
				File ff3 = new File(scalePath + "/" + user.getPhone()+"_100");
				Image src2 = bi.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
				BufferedImage dest2 = new BufferedImage(100, 100,
						BufferedImage.TYPE_INT_RGB);
				Graphics g2 = dest2.getGraphics();
				g2.drawImage(src2, 0, 0, null);
				g2.dispose();
				ImageIO.write(dest2, "png", ff3);

			} catch (IOException e) {
				logger.warn("更新用户，保存头像图片异常:" + e.getMessage());
			}
		}

		result = userService.updateUser(user);
		user.setTouxiang(null);
		return JSONObject.toJSONString(result);
	}

	@DELETE
	@Path("delete/{phone}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delUser(@PathParam("phone") String phone) {
		logger.info("请求删除用户信息:" + phone);
		ResultEntity<UserEntity> r = userService.deleteUser(phone);
		return JSONObject.toJSONString(r);
	}
	
	@GET
	@Path("get/{phone}")
	public String queryUser(@PathParam("phone") String phone){
		logger.info("请求查询用户信息:"+phone);
		ResultEntity<UserEntity> result = userService.queryUserBasicInfo(phone);
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@Path("count")
	public String countUser(){
		logger.info("请求按省份统计用户数!");
		ResultEntity<List<UserStasticEntity>> result = userService.countByProv();
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@Path("count/{prov}")
	public String countUserByProv(@PathParam("prov")int prov){
		logger.info("请求按省份城市统计用户数:"+prov);
		ResultEntity<List<UserStasticEntity>> result = userService.countByProvCity(prov);
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@Path("count/{prov}/{city}")
	public String countUserByProvCity(@PathParam("prov")int prov,@PathParam("city")int city){
		logger.info("请求按省份城市统计用户数:"+prov+"\t:"+city);
		ResultEntity<List<UserStasticEntity>> result = userService.countByProvCity(prov,city);
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@Path("list/{prov}/{city}")
	public String listUserByProvCity(@PathParam("prov")int prov,
			@PathParam("city")int city,
			@DefaultValue("0") @QueryParam("index") int index,
			@DefaultValue("50") @QueryParam("size") int size){
		logger.info("请求按省份城市获取用户列表:"+prov+"\t:"+city+"\t index:"+index+"\tsize:"+size);
		ResultEntity<List<UserEntity>> result = userService.listByProvCity(prov,city,index ,size);
		return JSONObject.toJSONString(result);
	}

	@GET
	@Path("tx/{phone}")
	@Produces(MediaType.TEXT_PLAIN)
	public String queryTouXiang(@PathParam("phone") String phone) {
		logger.debug("请求查询用户头像:" + phone);
		UserEntity u = userService.queryUser(phone);
		if (u != null)
			return u.getTouxiang();
		return "";
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(String entity){
		logger.info("请求登录:"+entity);
		
		UserEntity user = JSONObject.parseObject(entity, UserEntity.class);
		
		ResultEntity<UserEntity> r = userService.login(user);
		return JSONObject.toJSONString(r);
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String register(String entity) {
		logger.info("请求注册用户:" + entity);
		
		UserEntity user = new UserEntity();
		ResultEntity<UserEntity> result = new ResultEntity<UserEntity>();
		result.setData(user);
		result.setResult(ResultEntity.RESULT_FAIL);
		
		Map<String, String> map = null;
		try{
			map = JSONObject.parseObject(entity, HashMap.class);
		}catch(Exception e){
			logger.warn("注册请求信息json解析异常:"+e.getMessage());
			result.setReason("注册请求信息json解析异常:"+e.getMessage());
			return JSONObject.toJSONString(result);
		}
		// String code = map.get("code");
		String password = map.get("password");
		String phone = map.get("phone");

		user.setPhone(phone);
		user.setPassword(password);

		if (phone == null || "".equals(phone)) {
			result.setReason("手机号为空");
			return JSONObject.toJSONString(result);
		}

		UserEntity ue = userService.queryUser(phone);
		if (ue != null) {
			result.setReason("该手机号["+phone+"]已经注册过");
			result.setData(ue);
			return JSONObject.toJSONString(result);
		}

		if (user.getPassword() == null || "".equals(user.getPassword())) {
			result.setReason("密码不可以为空");
			return JSONObject.toJSONString(result);
		}

		result = userService.registerUser(user);
		return JSONObject.toJSONString(result);
	}

	// @POST
	// @Path("login")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public String login(String entity) {
	// logger.debug("请求登录:" + entity);
	// UserEntity user = null;
	// Response r = new Response();
	// r.setResult(0);
	// r.setUser(null);
	// r.setReason("");
	// try {
	// user = JSONObject.parseObject(entity, UserEntity.class);
	// } catch (Exception e) {
	// user = null;
	// r.setResult(0);
	// r.setReason("请求登录信息无效!");
	// }
	//
	// if (user != null) {
	// if (user.getUserId() == null || "".equals(user.getUserId())
	// || user.getPassword() == null
	// || "".equals(user.getPassword())) {
	// r.setResult(0);
	// r.setReason("用户名或密码为空!");
	//
	// } else {
	// UserEntity ue = userService.queryUser(user.getUserId());
	// if (ue != null) {
	// if (ue.getPassword().equals(user.getPassword())) {
	// r.setResult(1);
	// r.setReason("登录成功!");
	// r.setUser(ue);
	// } else {
	// r.setResult(0);
	// r.setReason("用户名或密码错误!");
	// }
	// } else {
	// r.setResult(0);
	// r.setReason("当前账号不存在!");
	// }
	// }
	//
	// }
	//
	// return JSONObject.toJSONString(r);
	// }
	//
	// class Response {
	// private int result;
	// private String reason;
	// private UserEntity user;
	//
	// public int getResult() {
	// return result;
	// }
	//
	// public void setResult(int result) {
	// this.result = result;
	// }
	//
	// public String getReason() {
	// return reason;
	// }
	//
	// public void setReason(String reason) {
	// this.reason = reason;
	// }
	//
	// public UserEntity getUser() {
	// return user;
	// }
	//
	// public void setUser(UserEntity user) {
	// this.user = user;
	// }
	// }

}
