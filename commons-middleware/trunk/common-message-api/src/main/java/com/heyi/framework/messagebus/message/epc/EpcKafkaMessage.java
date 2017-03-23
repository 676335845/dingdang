package com.heyi.framework.messagebus.message.epc;

import com.heyi.framework.messagebus.message.DefaultMessage;

public class EpcKafkaMessage extends DefaultMessage implements EpcMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8196418112450395939L;
	protected byte[] event;

	public byte[] getEvent() {
		return event;
	}

	public void setEvent(byte[] event) {
		this.event = event;
	}
}
