package com.heyi.framework.messagebus.message;

import java.io.Serializable;
import java.util.Date;

public abstract class DefaultMessage implements Serializable {
	/**
	 * 消息频道
	 */
	private String channel;
	/**
	 * 发送方
	 */
	private String sender;

	/**
	 * 发送时间
	 */
	private Date time;

	/**
	 * 业务号
	 */
	private String serviceCode;

	/**
	 * 唯一编号
	 */
	private String unid;

	/**
	 * 消息内容
	 */
	private String body;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
