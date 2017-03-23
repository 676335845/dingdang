package me.ywork.message.base;

/**
 * 群组钉消息头
 * 
 * @author TangGang 2015年8月2日
 * 
 */
public class DingGroupMessageHeader extends AbstractDingMessageHeader {
	private static final long serialVersionUID = -8745458148952561380L;

	/**
	 * 发送人员
	 */
	private String sender;

	/**
	 * 接受消息的群组ID
	 */
	private String cid;
	

	public DingGroupMessageHeader() {
		super();
	}

	public DingGroupMessageHeader(String agentid, DingMessageType msgtype) {
		super(agentid, msgtype);
	}

	public DingGroupMessageHeader(DingMessageType msgtype) {
		super(msgtype);
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}
