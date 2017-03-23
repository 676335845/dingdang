package me.ywork.salarybill.model;


import java.io.Serializable;

import com.alibaba.dubbo.common.utils.StringUtils;


/**
 * 薪资条主表
 */
public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3482567932753076819L;
	private String userId;
	private String deptName;
	private String userName;
	private String userJobNum;
	private String hierarchy;
	private String mobileNo;
	
	private String parentId;
	//private int pathLength;
	
	public String getUserId() {
		return userId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public String getUserJobNum() {
		if(StringUtils.isBlank(userJobNum)){
			return "";
		}
		return userJobNum;
	}
	public void setUserJobNum(String userJobNum) {
		this.userJobNum = userJobNum;
	}
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}