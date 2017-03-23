package me.ywork.message.base;

import java.io.Serializable;

/**
 * 钉消息头抽象类
 * 
 * @author TangGang 2015年8月2日
 * 
 */
public abstract class AbstractDingMessageHeader implements Serializable {

	private static final long serialVersionUID = -410275446075169325L;

	/**
	 * 钉消息类型，对应枚举{@link DingMessageType}中的枚举值
	 */
	private DingMessageType msgtype;

	/**
	 * 企业应用授权id，这个值代表以哪个应用的名义发送消息 <br/>
	 * 如果不填，则以当前获取accesstoken的应用作为发消息的应用
	 */
	private String agentid;
	

	public AbstractDingMessageHeader() {
		super();
	}

	public AbstractDingMessageHeader(DingMessageType msgtype) {
		super();
		this.setMsgtype(msgtype);
	}

	public AbstractDingMessageHeader(String agentid, DingMessageType msgtype) {
		super();
		this.setMsgtype(msgtype);
		this.setAgentid(agentid);
	}

	public DingMessageType getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(DingMessageType msgtype) {
		this.msgtype = msgtype;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

}
