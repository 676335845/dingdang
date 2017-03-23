package com.heyi.framework.messagebus.message.responsible;

import java.io.Serializable;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 主题消息
 * @author sulta
 *
 */
public class DefaultTopicMessage extends DefaultMessage implements Serializable{
	
	public DefaultTopicMessage() {
		super();
	}

	public DefaultTopicMessage(Object message) {
		super();
		this.message = message;
	}
		
	private static final long serialVersionUID = 3416799733300496282L;

	protected int type = Messages.Message_Type_Topic;
	
	protected Object message;
	
	
	public int getType() {
		return type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "DefaultTopicMessage [type=" + type + ", message=" + String.valueOf(message) + "]";
	}
}
