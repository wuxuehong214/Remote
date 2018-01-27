package com.jw.remote.entity;

import java.util.Date;

/**
 * 意见信息实体
 * @author Administrator
 *
 */
public class SuggestEntity {
	
	private int id;
	private Date sjc;
	private String content;
	private String phone;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getSjc() {
		return sjc;
	}
	public void setSjc(Date sjc) {
		this.sjc = sjc;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
