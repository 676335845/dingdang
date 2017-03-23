package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class SalSysFieldItemModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -867242155134827335L;
	/**
	 * 字段ID
	 */
	private String itemId;
	/**
	 * 字段名字
	 */
	@JSONField(name = "name")
	private String itemName;
	/**
	 * 字段值
	 */
	@JSONField(name = "value")
	private Double itemValue;
	/**
	 * 字段类型，0是增加，-1是减少
	 */
	private Short itemType;

	/**
	 * 与之相关的标识
	 */
	private String relativeId;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间 
	 */
	private Date modifiedDate;
	
	/**
	 * 企业的标识
	 */
	private String corpId;
	
	/**
	 * 扣款类型
	 */
	private Short deductType;
	
	/**
	 * 请假的天数：供员工自定义的请假字段所使用
	 */
	private Double attenDay;
	
	/**
	 * 
	 */
	
	public SalSysFieldItemModel() {
		super();
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}	

	public Double getItemValue() {
		return itemValue;
	}

	public Short getItemType() {
		return itemType;
	}

	public void setItemType(Short itemType) {
		this.itemType = itemType;
	}

	public String getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public Short getDeductType() {
		return deductType;
	}

	public void setDeductType(Short deductType) {
		this.deductType = deductType;
	}

	public Double getAttenDay() {
		return attenDay;
	}

	public void setAttenDay(Double attenDay) {
		this.attenDay = attenDay;
	}

	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}

}
