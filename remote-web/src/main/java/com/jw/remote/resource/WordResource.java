package com.jw.remote.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.jw.remote.entity.WordEntity;
import com.jw.remote.service.WordService;

@Path("word")
public class WordResource {
	
	private Logger logger = Logger.getLogger(WordResource.class);
	private WordService wordService = new WordService();
	
	@GET
	@Path("random/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public String randomWords(@PathParam("size") int size){
		logger.info("请求分词列表!");
		List<WordEntity> words = wordService.queryRandomWords(size);
		return JSONArray.toJSONString(words);
	}
	
	

}
