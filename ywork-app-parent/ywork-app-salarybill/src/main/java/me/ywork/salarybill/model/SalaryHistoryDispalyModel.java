package me.ywork.salarybill.model;


import java.io.Serializable;
import java.util.Date;


/**
 * 薪资条主表
 */
public class SalaryHistoryDispalyModel implements Serializable {
	
	
	private static final long serialVersionUID = 3727184501279287347L;
	
	private String logId;
	private String title;
	private Date createTime;
	
	
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
