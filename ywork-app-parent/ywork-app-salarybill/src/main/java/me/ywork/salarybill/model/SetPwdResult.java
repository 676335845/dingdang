package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 设置密码后返回的结果
 * @author lizh  2015年10月27日
 *
 */
public class SetPwdResult implements Serializable {

	private static final long serialVersionUID = 2758831477233347245L;

	/**
	 * 创建成功后的ID
	 */
	private String id;
	
	private Boolean sendDingMessage;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getSendDingMessage() {
		return sendDingMessage;
	}

	public void setSendDingMessage(Boolean sendDingMessage) {
		this.sendDingMessage = sendDingMessage;
	}

	public SetPwdResult() {
		super();
	}

	public SetPwdResult(String id, Boolean sendDingMessage,
			Integer receiverCount) {
		super();
		this.id = id;
		this.sendDingMessage = sendDingMessage;
	}

	
}
