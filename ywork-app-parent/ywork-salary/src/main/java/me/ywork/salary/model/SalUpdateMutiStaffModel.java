package me.ywork.salary.model;

import java.io.Serializable;
import java.util.List;

public class SalUpdateMutiStaffModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5514960111671105534L;
	private List<SalStaffAttendanceModel> staffAttendances;
	private List<SalStaffSalReportDetailModel> monthStaffSalDetailModels;
	private List<SalInfoDetailModel> staffSalInfoDetailModels;
	private List<String> corpList;
	/**
	 *  选人的类型，0是选择浮动款项的人，1是选择需要重置的的员工或部门
	 */
	private Short optType;
	/**
	 * 选人的时候，如果选择的是某个工资报表的浮动款项的人员或部门
	 */
	private String reportId;
	private List<SalStaffBaseInfoModel> staffBaseInfoModels;
	private List<SalCorpWhpRuleModel> SalCwhpRuleModels;
	private List<SalCorpDeductModel> salDeductList;
	private  SalCorpDeductModel salDeduct;
	private List<SalSysFieldItemModel>  saFieldList;
	

	public SalUpdateMutiStaffModel() {
		super();
	}

	public List<SalStaffAttendanceModel> getStaffAttendances() {
		return staffAttendances;
	}

	public void setStaffAttendances(List<SalStaffAttendanceModel> staffAttendances) {
		this.staffAttendances = staffAttendances;
	}

	public List<SalStaffSalReportDetailModel> getMonthStaffSalDetailModels() {
		return monthStaffSalDetailModels;
	}

	public void setMonthStaffSalDetailModels(List<SalStaffSalReportDetailModel> monthStaffSalDetailModels) {
		this.monthStaffSalDetailModels = monthStaffSalDetailModels;
	}

	public List<SalInfoDetailModel> getStaffSalInfoDetailModels() {
		return staffSalInfoDetailModels;
	}

	public void setStaffSalInfoDetailModels(List<SalInfoDetailModel> staffSalInfoDetailModels) {
		this.staffSalInfoDetailModels = staffSalInfoDetailModels;
	}

	public List<SalStaffBaseInfoModel> getStaffBaseInfoModels() {
		return staffBaseInfoModels;
	}

	public void setStaffBaseInfoModels(List<SalStaffBaseInfoModel> staffBaseInfoModels) {
		this.staffBaseInfoModels = staffBaseInfoModels;
	}

	public List<SalCorpWhpRuleModel> getSalCwhpRuleModels() {
		return SalCwhpRuleModels;
	}

	public void setSalCwhpRuleModels(List<SalCorpWhpRuleModel> salCwhpRuleModels) {
		SalCwhpRuleModels = salCwhpRuleModels;
	}

	public List<SalCorpDeductModel> getSalDeductList() {
		return salDeductList;
	}

	public void setSalDeductList(List<SalCorpDeductModel> salDeductList) {
		this.salDeductList = salDeductList;
	}

	public List<SalSysFieldItemModel> getSaFieldList() {
		return saFieldList;
	}

	public void setSaFieldList(List<SalSysFieldItemModel> saFieldList) {
		this.saFieldList = saFieldList;
	}

	public SalCorpDeductModel getSalDeduct() {
		return salDeduct;
	}

	public void setSalDeduct(SalCorpDeductModel salDeduct) {
		this.salDeduct = salDeduct;
	}

	public Short getOptType() {
		return optType;
	}

	public void setOptType(Short optType) {
		this.optType = optType;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public List<String> getCorpList() {
		return corpList;
	}

	public void setCorpList(List<String> corpList) {
		this.corpList = corpList;
	}
	
	
	
	
}
