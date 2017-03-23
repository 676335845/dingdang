package me.ywork.salary.entity;

import java.util.Date;

import me.ywork.base.entity.Entity;

public class SalStaffSalReportEntity extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4137207531922717347L;

	/**
	 * 企业月度工资报表标识
	 */
	private String salReportId;
	
	/**
	 * 企业ID
	 */
	private String corpId;
	
	/**
	 * 应发工资
	 */
	private Double shouldPaySal;
	/**
	 * 工资扣款
	 */
	private Double salDeduct;
	/**
	 * 代扣代缴
	 */
	private Double replaceDeduct;
	/**
	 * 实发工资
	 */
	private Double actualSal;
	/**
	 * 企业为员工缴纳的五险一金
	 */
	private Double corpInsuranceSal;
	/**
	 * 年终奖
	 */
	private Double annualBonus;

	/**
	 * 当月奖金
	 */
	private Double monthBonus;

	/**
	 * 其他税前补款
	 */
	private Double otherPretaxSal;

	/**
	 * 其他税后补款
	 */
	private Double otherAftertaxSal;
	/**
	 * 
	 * /** 其他税前扣款
	 */
	private Double otherPretaxDeduct;

	/**
	 * 其他税后扣款
	 */
	private Double otherAftertaxDeduct;
	
	/**
	 * 
	 */
	private String dingStaffId;
	
	/**
	 *  年月份
	 */
	private Date monthTime;
	
	/**
	 * 员工当月所缴纳的社保和公积金的款项
	 */
	private Double staffInsuranceSal;
	
	/**
	 * 员工当月所缴纳的个人所得税-
	 */
	private Double taxSal;
	
	/**
	 * 员工单月的考勤扣款
	 */
	private Double attenDeduct;
	
	public SalStaffSalReportEntity() {
		super();
	}

   

	public Double getShouldPaySal() {
		return shouldPaySal;
	}



	public void setShouldPaySal(Double shouldPaySal) {
		this.shouldPaySal = shouldPaySal;
	}



	public Double getSalDeduct() {
		return salDeduct;
	}



	public void setSalDeduct(Double salDeduct) {
		this.salDeduct = salDeduct;
	}



	public Double getReplaceDeduct() {
		return replaceDeduct;
	}



	public void setReplaceDeduct(Double replaceDeduct) {
		this.replaceDeduct = replaceDeduct;
	}



	public Double getActualSal() {
		return actualSal;
	}



	public void setActualSal(Double actualSal) {
		this.actualSal = actualSal;
	}



	public Double getAnnualBonus() {
		return annualBonus;
	}



	public void setAnnualBonus(Double annualBonus) {
		this.annualBonus = annualBonus;
	}



	public Double getMonthBonus() {
		return monthBonus;
	}



	public void setMonthBonus(Double monthBonus) {
		this.monthBonus = monthBonus;
	}



	public Double getOtherPretaxSal() {
		return otherPretaxSal;
	}



	public void setOtherPretaxSal(Double otherPretaxSal) {
		this.otherPretaxSal = otherPretaxSal;
	}



	public Double getOtherAftertaxSal() {
		return otherAftertaxSal;
	}



	public void setOtherAftertaxSal(Double otherAftertaxSal) {
		this.otherAftertaxSal = otherAftertaxSal;
	}



	public Double getOtherPretaxDeduct() {
		return otherPretaxDeduct;
	}



	public void setOtherPretaxDeduct(Double otherPretaxDeduct) {
		this.otherPretaxDeduct = otherPretaxDeduct;
	}



	public Double getOtherAftertaxDeduct() {
		return otherAftertaxDeduct;
	}



	public void setOtherAftertaxDeduct(Double otherAftertaxDeduct) {
		this.otherAftertaxDeduct = otherAftertaxDeduct;
	}



	public Date getMonthTime() {
		return monthTime;
	}

	public void setMonthTime(Date monthTime) {
		this.monthTime = monthTime;
	}

	public String getDingStaffId() {
		return dingStaffId;
	}

	public void setDingStaffId(String dingStaffId) {
		this.dingStaffId = dingStaffId;
	}

	public String getSalReportId() {
		return salReportId;
	}

	public void setSalReportId(String salReportId) {
		this.salReportId = salReportId;
	}



	public Double getCorpInsuranceSal() {
		return corpInsuranceSal;
	}



	public void setCorpInsuranceSal(Double corpInsuranceSal) {
		this.corpInsuranceSal = corpInsuranceSal;
	}



	public Double getStaffInsuranceSal() {
		return staffInsuranceSal;
	}



	public void setStaffInsuranceSal(Double staffInsuranceSal) {
		this.staffInsuranceSal = staffInsuranceSal;
	}



	public Double getTaxSal() {
		return taxSal;
	}



	public void setTaxSal(Double taxSal) {
		this.taxSal = taxSal;
	}



	public Double getAttenDeduct() {
		return attenDeduct;
	}



	public void setAttenDeduct(Double attenDeduct) {
		this.attenDeduct = attenDeduct;
	}



	public String getCorpId() {
		return corpId;
	}



	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	
}
