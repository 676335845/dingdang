package me.ywork.message.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 群组消息返回结果
 * @author TangGang  2015年8月3日
 *
 */
public class DingGroupMessageResult extends AbstractDingMessageResult {
	private static final long serialVersionUID = -5847521714018800017L;

	/**
	 * 接收者列表，以|分隔
	 */
	private String receiver;

	public DingGroupMessageResult() {
		super();
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public List<String> splitReceiverIds() {
		if (StringUtils.isNotEmpty(receiver)) {
			return Arrays.asList(receiver.split("\\|"));
		} else {
			return new ArrayList<String>();
		}
	}
	
}
