package com.heyi.framework.messagebus.message.responsible;

import org.kafka.message.PartitionedMessageKey;

/**
 * 可应答消息key
 * 
 * @author sulta
 *
 */
public class ResponsibleMessageKey extends PartitionedMessageKey {
	public ResponsibleMessageKey() {
	}
	
	private String serviceCode;
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Override
	public String toString() {
		return "ResponsibleMessageKey [key=" + getId() + ", responseTopic=" + getResponseTopic() + ", responsePartiton="
				+ getResponsePartiton() + ", isFinished=" + getIsFinished() + "]";
	}
}
