package me.ywork.org.api.model;

import java.io.Serializable;


/**
 * 身份
 * @author sulta
 *
 */
public class DingOrgActorVo implements Serializable {
	private static final long serialVersionUID = 4190747616944135010L;
	
	protected String id;

	protected String fdName;
	
	protected String fdDeptId;
	
	protected String fdDeptName;
	
	protected String fdUserId;
	protected String fdUserName;

	public String getFdDeptId() {
		return fdDeptId;
	}

	public void setFdDeptId(String fdDeptId) {
		this.fdDeptId = fdDeptId;
	}

	public String getFdDeptName() {
		return fdDeptName;
	}

	public void setFdDeptName(String fdDeptName) {
		this.fdDeptName = fdDeptName;
	}

	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	public String getFdUserName() {
		return fdUserName;
	}

	public void setFdUserName(String fdUserName) {
		this.fdUserName = fdUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	
}
