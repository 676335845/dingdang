package me.ywork.salarybill.entity;

import java.util.Date;

import me.ywork.base.entity.Entity;

/**
 * 薪资条主表
 */
public class SalaryBillLog extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	/**
	 * 企业号
	 */
	private String companyId;
	
	private String corpName;
	
	/**
	 * 操作人ID
	 */
	private String userId;
	
	/**
	 * 操作人姓名
	 */
	private String userName;
	
	/**
	 * 操作类型
	 */
	private String salaryType;
	
	/**
	 * 时间
	 */
	private String salaryMonth;
	
	/**
	 * 操作时间
	 */
	private Date createTime;
	
	/**
	 * oss文件
	 * @return
	 */
	private String fileKey;
	
	/**
	 * 删除标志
	 * @return
	 */
	private Short deleteFlag;
	
	/**
	 * 下发模板
	 */
	private String template;
	
	/**
	 * 通知标题 new add 160826
	 */
	private String title;
	
	/**
	 * 模板自定义标语 new add 160826
	 */
	private String signs;
	
	/**
	 * 本批次需要发送通知总量
	 */
	private int batchCount;
	
	/**
	 * 实际发送短信通知量
	 */
	private int smsCount;
	
	/**
	 * 发送状态 0待发送 1已发送
	 */
	private int smsStatus;
	
	
	public int getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public int getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(int smsStatus) {
		this.smsStatus = smsStatus;
	}

	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}

	public String getSigns() {
		return signs;
	}

	public void setSigns(String signs) {
		this.signs = signs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSalaryType() {
		return salaryType;
	}

	public void setSalaryType(String salaryType) {
		this.salaryType = salaryType;
	}

	public String getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Short getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Short deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public SalaryBillLog(){
		this.deleteFlag = 0;
		this.createTime = new Date();
	}
}
	
