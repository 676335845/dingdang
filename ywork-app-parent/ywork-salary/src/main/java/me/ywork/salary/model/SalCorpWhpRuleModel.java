package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;

public class SalCorpWhpRuleModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6485051169057394672L;
	/**
	 * 代缴代扣薪资规则标识
	 */
	private String id;
	/**
	 * 缴纳科目标识
	 */
	private String subjectId;
	
	/**
	 * 缴纳科目的名字
	 */
	private String subjectName;
	/**
	 * 基数下限
	 */
	private Double baseLow;
	/**
	 * 基数上限
	 */
	private Double baseHigh;
	/**
	 * 公司比例
	 */
	private Double corpPercent;
	/**
	 * 个人比例
	 */
	private Double personalPercent;
	/**
	 * 钉钉企业标识
	 */
	private String corpId;
	/**
	 * 代缴代扣薪资规则创建时间
	 */
	private Date createDate;
	/**
	 * 是否已经设置
	 */
	private Short hasSet;
	/**
	 * 代缴代扣薪资规则更新时间 
	 */
	private Date modifiedDate;
	
	
	public SalCorpWhpRuleModel() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
   
	public Double getBaseLow() {
		return baseLow;
	}
	public void setBaseLow(Double baseLow) {
		this.baseLow = baseLow;
	}
	public Double getBaseHigh() {
		return baseHigh;
	}
	public void setBaseHigh(Double baseHigh) {
		this.baseHigh = baseHigh;
	}
	public Double getCorpPercent() {
		return corpPercent;
	}
	public void setCorpPercent(Double corpPercent) {
		this.corpPercent = corpPercent;
	}
	public Double getPersonalPercent() {
		return personalPercent;
	}
	public void setPersonalPercent(Double personalPercent) {
		this.personalPercent = personalPercent;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Short getHasSet() {
		return hasSet;
	}
	public void setHasSet(Short hasSet) {
		this.hasSet = hasSet;
	}
}
