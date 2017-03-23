package me.ywork.message;

import me.ywork.message.base.DingGroupMessageHeader;
import me.ywork.message.base.DingMessageType;

/**
 * 群发OA消息
 * 
 * @author TangGang 2015年8月2日
 * 
 */
public class DingOAGroupMessage extends DingGroupMessageHeader {
	private static final long serialVersionUID = 1006102826128589083L;

	/**
	 * OA消息体
	 */
	private DingOAMessage oa;

	public DingOAGroupMessage(DingMessageType msgtype, DingOAMessage oa) {
		super(msgtype);

		this.oa = oa;
	}

	public DingOAGroupMessage(String agentid, DingMessageType msgtype,
			DingOAMessage oa) {
		super(agentid, msgtype);

		this.oa = oa;
	}

	public DingOAMessage getOa() {
		return oa;
	}

	public void setOa(DingOAMessage oa) {
		this.oa = oa;
	}

}
