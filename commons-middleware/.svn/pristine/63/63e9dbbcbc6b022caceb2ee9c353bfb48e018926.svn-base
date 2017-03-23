package com.heyi.framework.messagebus.message.responsible;

import java.io.Serializable;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 
 * @author sulta
 *
 */
public class DefaultResponseMessage extends DefaultMessage implements Serializable{
	
	public DefaultResponseMessage() {
		super();
	}
	
	public DefaultResponseMessage(Object response) {
		super();
		this.message = response;
	}
	
	private static final long serialVersionUID = 2319420257598990378L;

	protected int type = Messages.Message_Type_Response;
	
	protected int optCode;
	
	protected Object message;
	
	protected long timeStamp;
	
	public int getType() {
		return type;
	}

	public final void setType(int type) {
		return;
	}

	public int getOptCode() {
		return optCode;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return "DefaultResponseMessage [type=" + type + ", message=" + String.valueOf(message) + "]";
	}
}
