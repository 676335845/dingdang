package me.ywork.ticket.suite.model;

import java.io.Serializable;


public class QySuiteAgent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1612317762023868885L;
	public QySuiteAgent() {
	}
	private String agentId;
	private String name;
	private String logoUrl;
	private String appid;
	private String[] apiGroup;
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getApiGroup() {
		String v = "";
		for(String a : apiGroup){
			v+=a;
		}
		return v;
	}
	public void setApiGroup(String[] apiGroup) {
		this.apiGroup = apiGroup;
	}
	
}
