package com.jw.remote.entity.ext;

import com.jw.remote.entity.UserEntity;

public class GuanZhuListEleEntity {
	
	private long id;
	private UserEntity user;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
}
