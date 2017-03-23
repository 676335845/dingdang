package me.ywork.salarybill.model;


import java.io.Serializable;


/**
 * 薪资条主表
 */
public class SalaryItemDispalyModel implements Serializable {
	
	
	private static final long serialVersionUID = 3727184501279287347L;
	
	private String id;
	private String	salaryBillId;
	private String	itemName;
	private String	itemValue;
	private Integer	serNo;
	private String	companyId;
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
	
}
