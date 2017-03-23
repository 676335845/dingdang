package me.ywork.salary.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class SalCorpMbSalListModel {
	/*
	 * 月度工资报表Id
	 */
	@JSONField(name="id")
	private String id;
	
	/**
	 * 企业Id
	 */
	private String corpId;
	/**
	 * 工资报表的状态
	 */
	@JSONField(name="state")
	private Short salReportState;
	/**
	 * 应发工资
	 */
	@JSONField(name="yfgz")
	private Double shouldPaySal;
	
	/**
	 * 实发工资
	 */
	@JSONField(name="sfgz")
	private Double actualSal;;
	/**
	 * 公司交金
	 */
	@JSONField(name="gsjj")
	private Double insuranceSal;
	/**
	 * 员工成本
	 */
	@JSONField(name="ygcb")
	private Double staffCost;
	/**
	 * 工资报表产生时间
	 */
	private Date createDate;
	
	/**
	 * 工资报表的更新时间
	 */
	@JSONField(name="updateTime")
	private Date modifiedDate;
	
	/**
	 * 工资报表的月份
	 */
	@JSONField(name="date")
	private Date monthTime;
	
	/**
	 *  表格地址
	 */
	@JSONField(name="fileUrl")
	private String fileUrl;
	
	private Integer pageNo;
	private Integer pageSize;
	private Long totalCount;
	private Integer totalPages;
	private List<SalStaffSalReportModel> datas;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Short getSalReportState() {
		return salReportState;
	}
	public void setSalReportState(Short salReportState) {
		this.salReportState = salReportState;
	}
	public Double getShouldPaySal() {
		return shouldPaySal;
	}
	public void setShouldPaySal(Double shouldPaySal) {
		this.shouldPaySal = shouldPaySal;
	}
	public Double getInsuranceSal() {
		return insuranceSal;
	}
	public void setInsuranceSal(Double insuranceSal) {
		this.insuranceSal = insuranceSal;
	}
	public Double getStaffCost() {
		return staffCost;
	}
	public void setStaffCost(Double staffCost) {
		this.staffCost = staffCost;
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
	public Date getMonthTime() {
		return monthTime;
	}
	public void setMonthTime(Date monthTime) {
		this.monthTime = monthTime;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public List<SalStaffSalReportModel> getDatas() {
		return datas;
	}
	public void setDatas(List<SalStaffSalReportModel> datas) {
		this.datas = datas;
	}
	public Double getActualSal() {
		return actualSal;
	}
	public void setActualSal(Double actualSal) {
		this.actualSal = actualSal;
	}

}
