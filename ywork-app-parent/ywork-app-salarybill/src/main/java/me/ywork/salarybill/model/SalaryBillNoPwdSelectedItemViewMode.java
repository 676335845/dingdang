package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * 
 * @author kezm
 *
 *         2016年1月20日
 */
public class SalaryBillNoPwdSelectedItemViewMode implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3713921217921787889L;

	private String id;

	private String name;

	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



}
