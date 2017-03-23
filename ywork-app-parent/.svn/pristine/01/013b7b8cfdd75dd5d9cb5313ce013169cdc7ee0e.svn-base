package me.ywork.salary.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by xiaobai on 2017/1/11.
 */
public class SalStaffSalReportDetailModel extends SalStaffSalReportModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6257866169616226633L;
	/**
	 * 员工月工资ID
	 */
	@JSONField(name="id")
	private String id;;
	/**
	 * 年终奖
	 */
	@JSONField(name="nzj")
	private Double annualBonus;

	/**
	 * 当月奖金
	 */
	@JSONField(name="dyjj")
	private Double monthBonus;

	/**
	 * 其他税前补款
	 */
	@JSONField(name = "qtsqbk")
	private Double otherPretaxSal;

	/**
	 * 其他税后补款
	 */
	@JSONField(name = "qtshbk")
	private Double otherAftertaxSal;
	/**
	 * 
	 * /** 其他税前扣款
	 */
	@JSONField(name = "qtsqkk")
	private Double otherPretaxDeduct;

	/**
	 * 其他税后扣款
	 */
	@JSONField(name = "qtshkk")
	private Double otherAftertaxDeduct;
	
	

	public SalStaffSalReportDetailModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
