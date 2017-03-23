package com.heyi.framework.messagebus.message;

import java.io.Serializable;

import com.heyi.framework.messagebus.message.commitable.ICommitableMessage;

public abstract class OnsMessage implements Serializable , ICommitableMessage{

	/**
	 * 消息查找的key
	 */
	protected String messageKey;

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
	protected String messageTag;
	
	public String getMessageTag() {
		return messageTag;
	}

	public void setMessageTag(String messageTag) {
		this.messageTag = messageTag;
	}

	/**
	 * 定时消息，在指定的时间发送
	 */
	protected Long startDeliverTime;

	public Long getStartDeliverTime() {
		return startDeliverTime;
	}

	public void setStartDeliverTime(Long startDeliverTime) {
		this.startDeliverTime = startDeliverTime;
	}
	
	@Override
	public String toString() {
		return "DefaultMessage [" +
				"messageKey=" + messageKey + ", " +
				"messageTag" + messageTag + ", " +
				"startDeliverTime=" + startDeliverTime +			
				"]";
	}
}
