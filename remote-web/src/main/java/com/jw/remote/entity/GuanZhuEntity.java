package com.jw.remote.entity;

/**
 * 关注信息实体
 * @author Administrator
 *
 */
public class GuanZhuEntity {
	
	private long id;
	private long userid;   //关注者ID
	private long gzuserid;  //被关注者ID
	private int state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getGzuserid() {
		return gzuserid;
	}
	public void setGzuserid(long gzuserid) {
		this.gzuserid = gzuserid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

}
