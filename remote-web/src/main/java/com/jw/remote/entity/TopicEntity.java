package com.jw.remote.entity;

import java.util.Date;

/**
 * 远方内容
 * @author Administrator
 *
 */
public class TopicEntity {

	public static final int CHECK_WAIT = 0;    //待审核
	public static final int CHECK_SUCCESS = 1; //审核通过
	public static final int CHECK_FAIL = 2;  //审核未通过
	
	public static final int ORIGINAL_YES = 1;
	public static final int ORIGINAL_NO = 0;
	
	private long id;
	private String topic;
	private String emotion;
	private Date created_at;
	private Date updated_at;
	private  long userid;
	private String content;
	private int original;  // 1-原创  0-非原创
	private int check = CHECK_SUCCESS;  //是否需要审核
	
	private String phone;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getOriginal() {
		return original;
	}
	public void setOriginal(int original) {
		this.original = original;
	}
	
}
