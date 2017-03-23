package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;

public class SalCorpDeductModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 27753500244527056L;
	/**
	 *  标识
	 */
	private String id;
	/**
	 * 企业标识
	 */
	private String corpId;
	/**
	 * 迟到早退扣款方式
	 */
	private Short lateEarlyDeductType;
	/**
	 * 迟到早退扣款数额 元/次或 元/分钟
	 */
	private double  lateEarlyDeduct;
	/**
	 * 严重迟到扣款
	 */
	private double seriousLateDeduct;
	/**
	 * 旷工扣款
	 */
	private  double  stayAwayDeduct;
	
	/**
	 *   旷工扣款方式
	 */
	private Short  stayAwayDeductType;
	
	/**
	 *  缺卡扣款方式
	 */
	private Short lackDeductType;
	
	/**
	 *  缺卡扣款
	 */
	private double lackDeduct;
	
	/**
	 *   更新时间
	 */
	private Date modifiedDate;
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 是否已经设置
	 */
	private Short hasSet;
	
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
	public Short getLateEarlyDeductType() {
		return lateEarlyDeductType;
	}
	public void setLateEarlyDeductType(Short lateEarlyDeductType) {
		this.lateEarlyDeductType = lateEarlyDeductType;
	}
	public double getLateEarlyDeduct() {
		return lateEarlyDeduct;
	}
	public void setLateEarlyDeduct(double lateEarlyDeduct) {
		this.lateEarlyDeduct = lateEarlyDeduct;
	}
	public double getSeriousLateDeduct() {
		return seriousLateDeduct;
	}
	public void setSeriousLateDeduct(double seriousLateDeduct) {
		this.seriousLateDeduct = seriousLateDeduct;
	}
	public double getStayAwayDeduct() {
		return stayAwayDeduct;
	}
	public void setStayAwayDeduct(double stayAwayDeduct) {
		this.stayAwayDeduct = stayAwayDeduct;
	}
	public Short getStayAwayDeductType() {
		return stayAwayDeductType;
	}
	public void setStayAwayDeductType(Short stayAwayDeductType) {
		this.stayAwayDeductType = stayAwayDeductType;
	}
	public Short getLackDeductType() {
		return lackDeductType;
	}
	public void setLackDeductType(Short lackDeductType) {
		this.lackDeductType = lackDeductType;
	}
	public double getLackDeduct() {
		return lackDeduct;
	}
	public void setLackDeduct(double lackDeduct) {
		this.lackDeduct = lackDeduct;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Short getHasSet() {
		return hasSet;
	}
	public void setHasSet(Short hasSet) {
		this.hasSet = hasSet;
	}
}
