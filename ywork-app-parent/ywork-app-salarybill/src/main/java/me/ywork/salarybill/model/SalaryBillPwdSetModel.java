package me.ywork.salarybill.model;


import java.io.Serializable;
import java.util.Date;


/**
 * 用于员工查看自己薪资条和后台管理时使用的密码数据
 * 
 * */
public class SalaryBillPwdSetModel implements Serializable {
	
	private static final long serialVersionUID = -5870993437066009492L;
	private String id;
	private String companyId;
	private String userId;
	private Short passwordType;
	private String password;
	private String oldpass;
	private Boolean needReset;
	private String createUserId;
	private Date createTime;
	private String updateUserId;
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Short getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(Short passwordType) {
		this.passwordType = passwordType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getNeedReset() {
		return needReset;
	}

	public void setNeedReset(Boolean needReset) {
		this.needReset = needReset;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOldpass() {
		return oldpass;
	}

	public void setOldpass(String oldpass) {
		this.oldpass = oldpass;
	}

	

}
