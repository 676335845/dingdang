package me.ywork.salarybill.entity;

import me.ywork.base.entity.Entity;

/**
 * 悦通知薪酬管理员
 */
public class SalaryBillAdmin extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	/**
	 * 企业号
	 */
	private String companyId;
	
	/**
	 * 员工编号
	 */
	private String userId;
	
	/**
	 * 薪酬管理标识
	 */
	private boolean adminFlag;


	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(boolean adminFlag) {
		this.adminFlag = adminFlag;
	}
	

}
