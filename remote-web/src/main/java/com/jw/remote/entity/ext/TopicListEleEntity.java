package com.jw.remote.entity.ext;

import com.jw.remote.entity.TopicEntity;
import com.jw.remote.entity.UserEntity;

public class TopicListEleEntity {
	
	private TopicEntity topic;
	private UserEntity user;
	
	public TopicEntity getTopic() {
		return topic;
	}
	public void setTopic(TopicEntity topic) {
		this.topic = topic;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	

}
