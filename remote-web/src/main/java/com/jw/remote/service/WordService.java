package com.jw.remote.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.jw.remote.dao.WordDao;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.WordEntity;

public class WordService {
	
	private Logger logger = Logger.getLogger(WordService.class);
	private WordDao dao = new WordDao();
	
	/**
	 * 获取随机分词
	 * @param size
	 * @return
	 */
	public List<WordEntity> queryRandomWords(int size){
		 try {
			return dao.getRandomWords(size);
		} catch (SQLException | TimeoutException | NotInitializedException e) {
			logger.warn("获取随机分词时数据库操作异常:"+e.getMessage());
		}
		 return new ArrayList<WordEntity>();
	}

}
