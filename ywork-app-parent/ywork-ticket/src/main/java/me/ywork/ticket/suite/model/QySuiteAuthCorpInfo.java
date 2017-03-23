package me.ywork.ticket.suite.model;

import java.io.Serializable;

public class QySuiteAuthCorpInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2517910623872454356L;


	public QySuiteAuthCorpInfo() {
	}
	private String corpId; //授权方企业号id
	private String corpName; //授权方企业号名称
	private String logoUrl; //授权方头像
	private String industry;
	private String inviteCode;


	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	
	
	
	
}
