package me.ywork.salary.entity;

import me.ywork.base.entity.Entity;

public class SalCorpBaseRuleEntity extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1572222627582726528L;
	/**
	 * 薪资规则名称
	 */
	private String salRuleId;
	/**
	 * 薪资规则的企业ID
	 */
	private String corpId;
	/**
	 * 计薪天数
	 */
	private Double calSalDays;
	/**
	 * 试用人数
	 */
	private Integer fitNums;
	/**
	 * 是否已经设置
	 */
	private Short hasSet;


	public SalCorpBaseRuleEntity() {
		super();
	}
   
	public Double getCalSalDays() {
		return calSalDays;
	}

	public void setCalSalDays(Double calSalDays) {
		this.calSalDays = calSalDays;
	}
   
	

	public Integer getFitNums() {
		return fitNums;
	}

	public void setFitNums(Integer fitNums) {
		this.fitNums = fitNums;
	}

	public String getSalRuleId() {
		return salRuleId;
	}

	public void setSalRuleId(String salRuleId) {
		this.salRuleId = salRuleId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public Short getHasSet() {
		return hasSet;
	}

	public void setHasSet(Short hasSet) {
		this.hasSet = hasSet;
	}
}
