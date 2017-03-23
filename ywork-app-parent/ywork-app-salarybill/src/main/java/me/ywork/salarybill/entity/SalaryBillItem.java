package me.ywork.salarybill.entity;

import me.ywork.base.entity.Entity;

/**
 * 薪资条明细表
 */
public class SalaryBillItem extends Entity {
	
	private static final long serialVersionUID = -9007710287313362128L;
	
	/**
	 * 必填，工资明细所属工资条ID
	 */
	private String	salaryBillId;
	/**
	 * 必填，明细名称
	 */
	private String	itemName;
	
	/**
	 * 必填，明细值
	 */
	private String	itemValue;
	
	/**
	 * 必填，工资明细的显示顺序，一般为创建顺序，默认为1
	 */
	private Integer	serNo;
	
	/**
	 * 必填，所在公司在蓝凌应用中的内部ID
	 */
	private String	companyId;
	

	public SalaryBillItem() {
		super();
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
	public String toString() {
		return new StringBuilder(super.toString())
	       .append("\r\n")
		   .append("{")
		   .append("id=").append(this.getId())
		   .append(",salaryBillId=").append(this.getSalaryBillId())
		   .append(",itemName=").append(this.getItemName())
		   .append(",itemValue=").append(this.getItemValue())
		   .append(",companyId=").append(this.getCompanyId())
		   .append(",serNo=").append(this.getSerNo())
		   .append("}")
		   .toString();
	}


}
