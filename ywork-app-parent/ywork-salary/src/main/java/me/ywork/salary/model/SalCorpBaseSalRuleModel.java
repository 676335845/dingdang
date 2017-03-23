package me.ywork.salary.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 薪资规则的具体信息
 *
 * Created by xiaobai on 2017/1/11.
 */
public class SalCorpBaseSalRuleModel extends SalSysRuleModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6194590645604005116L;

	/**
	 * 企业薪资规则ID
	 */
	private String id;

	/**
	 * 计薪天数
	 */
	@JSONField(name = "days")
	private  Double calSalDays;
	/**
	 * 试用人数
	 */
	@JSONField(name = "count")
	private Integer fitNums;

	/**
	 * 规则对应的企业
	 */
	private String corpId;
	
	/**
	 * 系统或自定义的薪资规则
	 */
	private String salRuleId;
    
	/**
	 * 是否已经设置
	 */
	private Short hasSet;


	public SalCorpBaseSalRuleModel() {
		super();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getSalRuleId() {
		return salRuleId;
	}
	public void setSalRuleId(String salRuleId) {
		this.salRuleId = salRuleId;
	}
	public Short getHasSet() {
		return hasSet;
	}

	public void setHasSet(Short hasSet) {
		this.hasSet = hasSet;
	} 
}
