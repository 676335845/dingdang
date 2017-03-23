package com.heyi.message.timedtask;

import java.util.Date;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 定时任务闹钟
 * @author sulta
 *
 */
public class TimedTaskAlarmClock extends DefaultMessage{
	private static final long serialVersionUID = -1063378581899237237L;
	
	private String fdName;

	private String fdSystemName;
	
	private String fdTaskCategory;

	private String fdTaskKey;
	
	/**
	 * 任务创建日期
	 */
	private Date fdCreateTime;

	private byte[] fdTaskParam;

	public String getFdName() {
		return fdName;
	}
	
	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	public String getFdSystemName() {
		return fdSystemName;
	}
	public void setFdSystemName(String fdSystemName) {
		this.fdSystemName = fdSystemName;
	}
	public String getFdTaskCategory() {
		return fdTaskCategory;
	}
	public void setFdTaskCategory(String fdTaskCategory) {
		this.fdTaskCategory = fdTaskCategory;
	}
	public String getFdTaskKey() {
		return fdTaskKey;
	}
	public void setFdTaskKey(String fdTaskKey) {
		this.fdTaskKey = fdTaskKey;
	}
	public Date getFdCreateTime() {
		return fdCreateTime;
	}
	public void setFdCreateTime(Date fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}
	public byte[] getFdTaskParam() {
		return fdTaskParam;
	}
	public void setFdTaskParam(byte[] fdTaskParam) {
		this.fdTaskParam = fdTaskParam;
	}
	@Override
	public String toString() {
		return "TimedTaskAlarmClock [fdName=" + fdName + ", fdSystemName="
				+ fdSystemName + ", fdTaskCategory=" + fdTaskCategory
				+ ", fdTaskKey=" + fdTaskKey+
				", fdCreateTime="
				+ fdCreateTime + ", fdTaskParam=" + fdTaskParam + "]";
	}
}
