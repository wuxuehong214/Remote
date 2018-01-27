package com.jw.remote.entity.ext;

import com.alibaba.fastjson.JSONObject;
import com.jw.remote.entity.UserEntity;

/**
 * 结果实体
 * @author Administrator
 *
 */
public class ResultEntity<T> {
	
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_FAIL = 0;
	
	public static final int CODE_SQL_EXCEPTION = 1000;
	
	private int result = 1;
	private String reason;
	private int code;
	private T data;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public static void main(String args[]){
		ResultEntity<UserEntity> r = new ResultEntity<UserEntity>();
		r.setResult(1);
		r.setReason("dddd");
		UserEntity u = new UserEntity();
		u.setUsername("dasd");
		
		System.out.println(JSONObject.toJSONString(r));
		
	}
	
}
