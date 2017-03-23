package me.ywork.salarybill.model;


import java.io.Serializable;



public class OrgDeptModel implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3655628571524211044L;
	private String id;
	private String deptName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
	
	
	
	
}