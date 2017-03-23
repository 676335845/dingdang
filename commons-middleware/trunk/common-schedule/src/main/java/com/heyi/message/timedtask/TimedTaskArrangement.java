package com.heyi.message.timedtask;

import java.util.Date;

import com.heyi.framework.messagebus.message.DefaultMessage;

/**
 * 分布式定时任务安排
 * @author sulta
 *
 */
public class TimedTaskArrangement extends DefaultMessage{
	private static final long serialVersionUID = -2442209923357684232L;
	private String fdName;
	private String fdSystemName;
	private String fdTaskCategory;
	private String fdTaskKey;
	private String fdTaskChannel;
	private Date fdCreateTime;
	private byte[] fdTaskParam;
	private Date fdTriggerTime;
	
	/**
	 * 是否取消已有的安排
	 */
	private Boolean fdIsCanceled;
	/**
	 * 是否重复执行
	 */
	private Boolean fdIsRepeat = false;
	/**
	 * 重复间隔
	 */
	private Integer fdPeriod;
	/**
	 * 重复次数，设置一个足够大的数字可以让任务永不停止
	 */
	private Integer fdRepeatTimes;
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
	public Date getFdTriggerTime() {
		return fdTriggerTime;
	}
	public void setFdTriggerTime(Date fdTriggerTime) {
		this.fdTriggerTime = fdTriggerTime;
	}
	public Boolean getFdIsRepeat() {
		return fdIsRepeat;
	}
	public void setFdIsRepeat(Boolean fdIsRepeat) {
		this.fdIsRepeat = fdIsRepeat;
	}
	public Integer getFdPeriod() {
		return fdPeriod;
	}
	public void setFdPeriod(Integer fdPeriod) {
		this.fdPeriod = fdPeriod;
	}
	public Integer getFdRepeatTimes() {
		return fdRepeatTimes;
	}
	public void setFdRepeatTimes(Integer fdRepeatTimes) {
		this.fdRepeatTimes = fdRepeatTimes;
	}
	public Boolean getFdIsCanceled() {
		return fdIsCanceled;
	}
	public void setFdIsCanceled(Boolean fdIsCanceled) {
		this.fdIsCanceled = fdIsCanceled;
	}
	public String getFdTaskChannel() {
		return fdTaskChannel;
	}
	public void setFdTaskChannel(String fdTaskChannel) {
		this.fdTaskChannel = fdTaskChannel;
	}
}
