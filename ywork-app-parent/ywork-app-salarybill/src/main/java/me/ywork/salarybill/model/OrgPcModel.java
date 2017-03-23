package me.ywork.salarybill.model;

import java.io.Serializable;

/**
 * pc端人员组织
 * 
 * @Description: TODO
 * @author kezm
 * @date 2016年1月13日
 *
 */
public class OrgPcModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4853105173707471263L;

	private String id;

	private String orgType;

	private String text;

	private String parent;

	private String dingId;

	private String icon;

	private boolean children;

	private OrgPcSelectedStatusModel state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getDingId() {
		return dingId;
	}

	public void setDingId(String dingId) {
		this.dingId = dingId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isChildren() {
		return children;
	}

	public void setChildren(boolean children) {
		this.children = children;
	}

	public OrgPcSelectedStatusModel getState() {
		return state;
	}

	public void setState(OrgPcSelectedStatusModel state) {
		this.state = state;
	}

}
