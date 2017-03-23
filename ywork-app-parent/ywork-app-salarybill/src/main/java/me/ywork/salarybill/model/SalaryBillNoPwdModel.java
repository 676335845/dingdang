package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 
 * @author kezm
 *
 *         2016年1月20日
 */
public class SalaryBillNoPwdModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5703284329401137650L;

	private String companyId;

	private String id;

	private String userId;

	private String name;

	private String type;

	private String hierarchyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(String hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
