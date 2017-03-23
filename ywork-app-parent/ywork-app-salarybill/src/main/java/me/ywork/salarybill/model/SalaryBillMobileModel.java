package me.ywork.salarybill.model;


import java.io.Serializable;


/**
 * 上传的手机信息
 */
public class SalaryBillMobileModel implements Serializable {
	
	private static final long serialVersionUID = -3832580988196376728L;
	private String id;
	private String companyId;
	private String userId;
	private String userName;
	private String userDept;
	private String userJobNo;
	private String mobileNo;
	private String reason;
	

	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserDept() {
		return userDept;
	}


	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}


	public String getUserJobNo() {
		return userJobNo;
	}


	public void setUserJobNo(String userJobNo) {
		this.userJobNo = userJobNo;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public SalaryBillMobileModel(){
		
	}
}
