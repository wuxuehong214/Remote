package com.jw.remote.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jw.remote.entity.SuggestEntity;
import com.jw.remote.entity.ext.ResultEntity;
import com.jw.remote.service.SuggestService;

@Path("suggest")
public class SuggestResource {
	
	private Logger logger = Logger.getLogger(SuggestResource.class);
	private SuggestService service = new SuggestService();
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addSuggest(String entity){
		logger.info("发布反馈意见:"+entity);
		SuggestEntity suggest = JSONObject.parseObject(entity, SuggestEntity.class);
		suggest.setSjc(new Date());
		ResultEntity<SuggestEntity> result = service.addSuggest(suggest);
		return JSONObject.toJSONString(result);
	}

}
