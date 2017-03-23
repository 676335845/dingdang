package me.ywork.salarybill.entity;

import me.ywork.base.entity.Entity;

/**
 * 悦通知薪酬管理员
 */
public class SalaryBillMobileNo extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	private String companyId;
	
	private String userId;
	
	private String dept;
	
	private String name;
	
	private String jobNo;
	
	private String mobileNo;
	
	private String createUserId;//提交者
	
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	

}
