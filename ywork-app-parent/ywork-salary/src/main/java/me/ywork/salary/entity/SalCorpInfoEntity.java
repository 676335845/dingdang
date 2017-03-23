package me.ywork.salary.entity;

import me.ywork.base.entity.Entity;

public class SalCorpInfoEntity extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 470645865738445853L;
	/**
	 * 企业ID
	 */
	private String corpId;
	/**
	 * 企业的密码锁开启状态
	 */
	private Short passState;
	
	public SalCorpInfoEntity() {
		super();
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Short getPassState() {
		return passState;
	}
	public void setPassState(Short passState) {
		this.passState = passState;
	}

	

}
