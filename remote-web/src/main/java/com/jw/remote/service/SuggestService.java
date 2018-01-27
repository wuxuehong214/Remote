package com.jw.remote.service;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.jw.remote.dao.SuggestDao;
import com.jw.remote.db.NotInitializedException;
import com.jw.remote.entity.SuggestEntity;
import com.jw.remote.entity.ext.ResultEntity;

public class SuggestService {
	
	private Logger logger = Logger.getLogger(SuggestService.class);
	private SuggestDao dao = new SuggestDao();
	
	/**
	 * 新增反馈意见
	 * @param suggest
	 * @return
	 */
	public ResultEntity<SuggestEntity>  addSuggest(SuggestEntity suggest){
		ResultEntity<SuggestEntity> result = new ResultEntity<SuggestEntity>();
		try {
			dao.addSuggest(suggest);
			result.setResult(ResultEntity.RESULT_SUCCESS);
			result.setData(suggest);
		} catch (TimeoutException | NotInitializedException | SQLException e) {
			logger.warn("新增反馈意见时数据库操作异常!",e);
			result.setResult(ResultEntity.RESULT_FAIL);
			result.setReason("新增反馈意见时数据库操作异常!");
			result.setCode(ResultEntity.CODE_SQL_EXCEPTION);
		}
		return result;
	}

}
