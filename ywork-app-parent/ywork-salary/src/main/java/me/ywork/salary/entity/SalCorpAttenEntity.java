package me.ywork.salary.entity;

import java.util.Date;

import me.ywork.base.entity.Entity;

public class SalCorpAttenEntity  extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7334190841893697652L;
	/**
	 * 员工月度考勤ID
	 */
	private String attenReportId;
	/**
	 * 悦工作钉钉号
	 */
	private String corpId;
	/**
	 * 员工标识
	 */
	private String dingStaffId;
	/**
	 * 出勤天数
	 */
	private Double attendanceDays;
	/**
	 * 休息天数
	 */
	private Double restDays;
	/**
	 * 工作时长
	 */
	private Double workHours;
	/**
	 * 迟到时长
	 */
	private Double lateHours;
	/**
	 * 迟到次数
	 */
	private Double lateTimes;
	/**
	 * 严重迟到时间
	 */
	private Double seriousLateHours;
	/**
	 * 严重迟到次数
	 */
	private Double seriousLateTimes;
	/**
	 * 早退时间
	 */
	private Double earlyLeaveHours;
	/**
	 * 早退次数
	 */
	private Double  earlyLeaveTimes;
	/**
	 * 上班缺卡次数
	 */
	private Double workAbsenceTimes;
	/**
	 * 下班缺卡次数
	 */
	private Double offWorkAbsenceTimes;
	/**
	 * 一天未打卡天数
	 */
	private Double unWorkDays;
	/**
	 * 旷工迟到天数
	 */
	private Double unWorkLateDays;
	/**
	 * 年月份
	 */
	private Date monthTime;
	
	public SalCorpAttenEntity() {
		super();
	}
	public String getAttenReportId() {
		return attenReportId;
	}
	public void setAttenReportId(String attenReportId) {
		this.attenReportId = attenReportId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
   
	public String getDingStaffId() {
		return dingStaffId;
	}
	public void setDingStaffId(String dingStaffId) {
		this.dingStaffId = dingStaffId;
	}
    
	public Double getAttendanceDays() {
		return attendanceDays;
	}
	public void setAttendanceDays(Double attendanceDays) {
		this.attendanceDays = attendanceDays;
	}
	public Double getRestDays() {
		return restDays;
	}
	public void setRestDays(Double restDays) {
		this.restDays = restDays;
	}
	public Double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
	public Double getLateHours() {
		return lateHours;
	}
	public void setLateHours(Double lateHours) {
		this.lateHours = lateHours;
	}
	public Double getLateTimes() {
		return lateTimes;
	}
	public void setLateTimes(Double lateTimes) {
		this.lateTimes = lateTimes;
	}
	public Double getSeriousLateHours() {
		return seriousLateHours;
	}
	public void setSeriousLateHours(Double seriousLateHours) {
		this.seriousLateHours = seriousLateHours;
	}
	public Double getSeriousLateTimes() {
		return seriousLateTimes;
	}
	public void setSeriousLateTimes(Double seriousLateTimes) {
		this.seriousLateTimes = seriousLateTimes;
	}
	public Double getEarlyLeaveHours() {
		return earlyLeaveHours;
	}
	public void setEarlyLeaveHours(Double earlyLeaveHours) {
		this.earlyLeaveHours = earlyLeaveHours;
	}
	public Double getEarlyLeaveTimes() {
		return earlyLeaveTimes;
	}
	public void setEarlyLeaveTimes(Double earlyLeaveTimes) {
		this.earlyLeaveTimes = earlyLeaveTimes;
	}
	public Double getWorkAbsenceTimes() {
		return workAbsenceTimes;
	}
	public void setWorkAbsenceTimes(Double workAbsenceTimes) {
		this.workAbsenceTimes = workAbsenceTimes;
	}
	public Double getOffWorkAbsenceTimes() {
		return offWorkAbsenceTimes;
	}
	public void setOffWorkAbsenceTimes(Double offWorkAbsenceTimes) {
		this.offWorkAbsenceTimes = offWorkAbsenceTimes;
	}
	public Double getUnWorkDays() {
		return unWorkDays;
	}
	public void setUnWorkDays(Double unWorkDays) {
		this.unWorkDays = unWorkDays;
	}
	public Double getUnWorkLateDays() {
		return unWorkLateDays;
	}
	public void setUnWorkLateDays(Double unWorkLateDays) {
		this.unWorkLateDays = unWorkLateDays;
	}
	public Date getMonthTime() {
		return monthTime;
	}
	public void setMonthTime(Date monthTime) {
		this.monthTime = monthTime;
	}
}
