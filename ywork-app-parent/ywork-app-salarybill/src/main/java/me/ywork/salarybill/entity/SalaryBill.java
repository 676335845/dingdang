package me.ywork.salarybill.entity;

import java.util.Date;

import me.ywork.base.entity.Entity;

/**
 * 薪资条主表
 */
public class SalaryBill extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	/**
	 * 企业号
	 */
	private String companyId;
	
	/**
	 * 员工编号
	 */
	private String userId;
	
	/**
	 * 部门
	 */
	private String deptId;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 工号
	 */
	private String userJobNum;
	
	/**
	 * 薪资月份
	 */
	private String salaryMonth;
	
	/**
	 * 薪资类型
	 */
	private String salaryType;
	
	/**
	 * 税前工资
	 */
	private String pretaxSalary;
	
	/**
	 * 实发工资
	 */
	private String realSalary;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建人员
	 */
	private String createUserId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 操作批次
	 */
	private String salaryBillLogId;
	
	/**
	 * 列名
	 */
	private String columnName;
	
	/**
	 * 已读标志
	 */
	private Boolean readFlag;
	
	private Boolean deleteFlag; //撤回标识
	
	/**
	 * 是否发送短信通知
	 */
	private boolean sendSms;
	
	
	private String smsContent; //发送短信全文...
	
	
	
	
	public Boolean getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public boolean isSendSms() {
		return sendSms;
	}
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}
	public Boolean getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(Boolean readFlag) {
		this.readFlag = readFlag;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getSalaryBillLogId() {
		return salaryBillLogId;
	}
	public void setSalaryBillLogId(String salaryBillLogId) {
		this.salaryBillLogId = salaryBillLogId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserJobNum() {
		return userJobNum;
	}
	public void setUserJobNum(String userJobNum) {
		this.userJobNum = userJobNum;
	}
	public String getSalaryMonth() {
		return salaryMonth;
	}
	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}
	public String getSalaryType() {
		return salaryType;
	}
	public void setSalaryType(String salaryType) {
		this.salaryType = salaryType;
	}
	public String getPretaxSalary() {
		return pretaxSalary;
	}
	public void setPretaxSalary(String pretaxSalary) {
		this.pretaxSalary = pretaxSalary;
	}
	public String getRealSalary() {
		return realSalary;
	}
	public void setRealSalary(String realSalary) {
		this.realSalary = realSalary;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
