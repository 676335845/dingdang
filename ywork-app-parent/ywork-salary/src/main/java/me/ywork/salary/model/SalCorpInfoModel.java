package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class SalCorpInfoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8553807088261744548L;
	/**
	 * 企业信息的主键
	 */
	private String id;
	/**
	 * 企业ID
	 */
	private String corpId;
	/**
	 * 公司的名称
	 */
	private String corpName;
	/**
	 * 企业信息的创建时间
	 */
	private Date createDate;
	/**
	 * 企业信息的更新时间
	 */
	private Date modifiedDate;
	/**
	 * 企业的密码锁开启状态
	 */
	@JSONField(name = "status")
	private Short passState;
	
	
	public SalCorpInfoModel() {
		super();
	}
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
	public Short getPassState() {
		return passState;
	}
	public void setPassState(Short passState) {
		this.passState = passState;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	
}
