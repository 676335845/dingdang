package me.ywork.org.api.message;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 钉钉管理员用户登陆微应用-消息
 * @author sulta
 *
 */
public class DingAdminLogonMessage extends DefaultMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5763457750108548253L;
	
	private String corpId;
	
	private String userId;
	
	private boolean isAdmin;
	
	private Integer adminLevel;

	public String getCorpId() {
		return corpId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAdminLevel() {
		return adminLevel;
	}

	public void setAdminLevel(Integer adminLevel) {
		this.adminLevel = adminLevel;
	}
	
}
