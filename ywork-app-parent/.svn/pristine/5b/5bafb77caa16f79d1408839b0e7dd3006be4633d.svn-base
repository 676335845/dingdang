package me.ywork.salarybill.model;


import java.io.Serializable;

import com.heyi.utils.IGroupable;


/**
 * 薪资条主表
 */
public class SalaryBillItemModel implements Serializable ,IGroupable<String>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -209203563734739565L;
	private String id;
	private String	salaryBillId;
	private String	itemName;
	private String	itemValue;
	private Integer	serNo;
	private String	companyId;
	private String realSalary;
	private String billId;

	
	public String getRealSalary() {
		return realSalary;
	}

	public void setRealSalary(String realSalary) {
		this.realSalary = realSalary;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalaryBillId() {
		return salaryBillId;
	}

	public void setSalaryBillId(String salaryBillId) {
		this.salaryBillId = salaryBillId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public Integer getSerNo() {
		return serNo;
	}

	public void setSerNo(Integer serNo) {
		this.serNo = serNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String getGroupKey() {
		return salaryBillId;
	}


}
