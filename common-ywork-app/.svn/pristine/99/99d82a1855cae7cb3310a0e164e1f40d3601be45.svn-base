package me.ywork.message.base;

import java.util.List;

/**
 * 钉钉企业消息头
 * @author TangGang  2015年9月1日
 *
 */
public class DingCorpMessageHeader extends AbstractDingMessageHeader {
	private static final long serialVersionUID = -3303175439703826675L;
	
	/**
	 * 员工ID列表（消息接收者，多个接收者用’ | ‘分隔）。特殊情况：指定为@all，则向该企业应用的全部成员发送
	 */
	private String touser;
	
	/**
	 * 部门ID列表，多个接收者用’ | '分隔。当touser为@all时忽略本参数 touser或者toparty 二者有一个必填
	 */
	private String toparty;
	
	public DingCorpMessageHeader() {
		super();
	}

	public DingCorpMessageHeader(DingMessageType msgtype) {
		super(msgtype);
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}
	
	/**
	 * 设置接收企业消息的员工ID
	 * @param ids  员工ID列表
	 */
	public void appendReceiveUserIds(List<String> ids) {
		if (ids == null) {
			throw new NullPointerException(
					"appendReceverUserIds - parameter ids is null.");
		}
		
		boolean hasUser = false;
		StringBuilder idBuilder = new StringBuilder();
		for (String id : ids) {
			if (hasUser) {
				idBuilder.append("|");
			}
			
			idBuilder.append(id);
			
			hasUser = true;
		}
		
		this.touser = idBuilder.toString();
	}
	
	/**
	 * 设置接收企业消息的员工ID
	 * @param users  员工列表
	 */
	public <E extends IDingMessageUser> void appendReceiveUsers(List<E> users) {
		if (users == null) {
			throw new NullPointerException(
					"appendReceverUsers - parameter users is null.");
		}
		
		boolean hasUser = false;
		StringBuilder idBuilder = new StringBuilder();
		for (E e : users) {
			if (hasUser) {
				idBuilder.append("|");
			}
			
			idBuilder.append(e.getDingUserId());
			
			hasUser = true;
		}
		
		this.touser = idBuilder.toString();
	}
	
	/**
	 * 设置接收企业消息的部门ID
	 * @param ids  部门ID列表
	 */
	public void appendReceiveDeptIds(List<String> ids) {
		if (ids == null) {
			throw new NullPointerException(
					"appendReceiveDeptIds - parameter ids is null.");
		}
		
		boolean hasUser = false;
		StringBuilder idBuilder = new StringBuilder();
		for (String id : ids) {
			if (hasUser) {
				idBuilder.append("|");
			}
			
			idBuilder.append(id);
			
			hasUser = true;
		}
		
		this.toparty = idBuilder.toString();
	}
	
	/**
	 * 设置接收企业消息的部门ID
	 * @param depts  员工列表
	 */
	public <E extends IDingMessageDept> void appendReceiveDepts(List<E> depts) {
		if (depts == null) {
			throw new NullPointerException(
					"appendReceiveDepts - parameter users is null.");
		}
		
		boolean hasUser = false;
		StringBuilder idBuilder = new StringBuilder();
		for (E e : depts) {
			if (hasUser) {
				idBuilder.append("|");
			}
			
			idBuilder.append(e.getDingDeptId());
			
			hasUser = true;
		}
		
		this.toparty = idBuilder.toString();
	}
}
