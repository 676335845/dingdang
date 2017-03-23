package me.ywork.salary.entity;

import me.ywork.base.entity.Entity;

public class SalElementInfoEntity extends Entity {
   /**
	 * 
	 */
	private static final long serialVersionUID = -9191030403989195520L;
/**
    * 企业ID
    */
   private String fdOrgId;
   /**
    *组织架构的 类型
    */
   private Short fdOrgType;
   /**
    * 名字
    */
   private String fdName;
   /**
    * 钉钉ID
    */
   private String fdDingId;
   /**
    * 父ID
    */
   private String fdParentId;
   /**
    * 部门名字
    */
   private String deptName;
   /**
    * 工号
    */
   private String userJobNum;

	public String getFdOrgId() {
		return fdOrgId;
	}
	public void setFdOrgId(String fdOrgId) {
		this.fdOrgId = fdOrgId;
	}
	
	public Short getFdOrgType() {
		return fdOrgType;
	}
	public void setFdOrgType(Short fdOrgType) {
		this.fdOrgType = fdOrgType;
	}
	public String getFdName() {
		return fdName;
	}
	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	public String getFdDingId() {
		return fdDingId;
	}
	public void setFdDingId(String fdDingId) {
		this.fdDingId = fdDingId;
	}
	public String getFdParentId() {
		return fdParentId;
	}
	public void setFdParentId(String fdParentId) {
		this.fdParentId = fdParentId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUserJobNum() {
		return userJobNum;
	}
	public void setUserJobNum(String userJobNum) {
		this.userJobNum = userJobNum;
	}		
}
