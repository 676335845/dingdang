package me.ywork.salarybill.model;

import java.io.Serializable;

import com.heyi.utils.IGroupable;

/**
 * 密码校验提示
 * @author lizh  2015年10月27日
 *
 */
public class CorpAdminMessage implements Serializable,IGroupable<String> {

	private String company;
	
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String getGroupKey() {
		return company;
	}
	
}
