package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 
 * @author kezm
 *
 * 2016年1月20日
 */
public class SalaryBillNoPwdItemModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1743341629638060690L;

	private String orguid;

	private String name;

	private String type;

	private String companyId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOrguid() {
		return orguid;
	}

	public void setOrguid(String orguid) {
		this.orguid = orguid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
