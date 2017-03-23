package me.ywork.message.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 企业消息返回结果数据结构
 * @author TangGang  2015年9月1日
 *
 */
public class DingCorpMessageResult extends AbstractDingMessageResult {
	private static final long serialVersionUID = -5601718985982473695L;
	
    /**
     * 企业消息发送失败的人员列表，格式为UserID1|UserID2
     */
    private String invaliduser;
    /**
     * 企业消息发送失败的部门列表，格式为PartyID1|PartyID2
     */
    private String invalidparty;

	public DingCorpMessageResult() {
		super();
	}

	public String getInvalidparty() {
		return invalidparty;
	}

	public void setInvalidparty(String invalidparty) {
		this.invalidparty = invalidparty;
	}

	public String getInvaliduser() {
		return invaliduser;
	}

	public void setInvaliduser(String invaliduser) {
		this.invaliduser = invaliduser;
	}
		
	public List<String> splitInvalidUserIds() {
		if (StringUtils.isNotEmpty(invaliduser)) {
			return Arrays.asList(invaliduser.split("\\|"));
		} else {
			return new ArrayList<String>();
		}
	}
	
	public List<String> splitInvalidDeptIds() {
		if (StringUtils.isNotEmpty(invalidparty)) {
			return Arrays.asList(invalidparty.split("\\|"));
		} else {
			return new ArrayList<String>();
		}
	}
}
