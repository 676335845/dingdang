package me.ywork.ticket.suite.model;

import java.io.Serializable;

public class QySuiteDepartment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6452157597680689135L;
	public QySuiteDepartment() {
	}
	private String id;
	private String name;
	private String parentid;
	private boolean writeble;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public boolean isWriteble() {
		return writeble;
	}
	public void setWriteble(boolean writeble) {
		this.writeble = writeble;
	}
	
}
