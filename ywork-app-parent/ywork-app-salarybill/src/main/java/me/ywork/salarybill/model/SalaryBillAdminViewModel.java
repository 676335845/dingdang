package me.ywork.salarybill.model;

import com.alibaba.fastjson.annotation.JSONField;

import me.ywork.base.entity.Entity;

/**
 * 悦通知薪酬管理员
 */
public class SalaryBillAdminViewModel extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	/**
	 * 员工编号
	 */
	@JSONField(name="uid")
	private String userId;
	
	/**
	 * 姓名
	 */
	@JSONField(name="un")
	private String userName;
	
	/**
	 * 薪酬管理标识
	 */
	@JSONField(name="af")
	private boolean adminFlag;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(boolean adminFlag) {
		this.adminFlag = adminFlag;
	}
	

}
