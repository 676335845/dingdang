package me.ywork.org.api.model;

import java.io.Serializable;

/**
 * 钉钉通讯录部门
 * 
 * @author Key
 * 
 */
public class DingOrgDeptVo implements Serializable  {
	public DingOrgDeptVo() {
	}
	
	protected String fdId;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	/**
	 * 部门钉钉id
	 */
	protected String dingDeptId;

	public String getDingDeptId() {
		return dingDeptId;
	}

	public void setDingDeptId(String dingDeptId) {
		this.dingDeptId = dingDeptId;
	}

	/**
	 * 部门名称
	 */
	public String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 父部门Id
	 */
	public String parentDeptId;

	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	/**
	 * 所属公司id
	 */
	public String corpId;

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
}
