package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 
 * @author kezm
 *
 * 2016年1月20日
 */
public class SalaryBillPwdTempModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2816517706387918304L;
	private String companyId;
	private String userId;
	private Short passwordType;
	private Boolean isNeedPasswd;

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

	public Boolean getIsNeedPasswd() {
		return isNeedPasswd;
	}

	public void setIsNeedPasswd(Boolean isNeedPasswd) {
		this.isNeedPasswd = isNeedPasswd;
	}

	public Short getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(Short passwordType) {
		this.passwordType = passwordType;
	}

}
