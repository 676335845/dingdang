package com.heyi.framework.messagebus.message;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 跨系统事件消息
 * 
 * @author sulta
 *
 */
public class PlatformEventMessage extends DefaultMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4949388774193955160L;

	private byte[] data;
	
	/**
	 * 事件发起方的groupid
	 */
	private String appGroupId;
	
	/**
	 * 指定接收方系统的groupid，如果为空则所有group都可以接收
	 */
	private String platformScope;
	
	/**
	 * 指定行政组织接收范围,只有指定范围可以接收
	 */
	private String organizationScope;	
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getAppGroupId() {
		return appGroupId;
	}

	public void setAppGroupId(String appGroupId) {
		this.appGroupId = appGroupId;
	}

	public String getPlatformScope() {
		return platformScope;
	}

	public void setPlatformScope(String platformScope) {
		this.platformScope = platformScope;
	}

	public String getOrganizationScope() {
		return organizationScope;
	}

	public void setOrganizationScope(String organizationScope) {
		this.organizationScope = organizationScope;
	}
}
