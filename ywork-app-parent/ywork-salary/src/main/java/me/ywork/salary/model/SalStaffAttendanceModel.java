package me.ywork.salary.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 考勤表人员的月度具体信息
 *
 * Created by xiaobai on 2017/1/11.
 */
public class SalStaffAttendanceModel extends SalStaffBaseInfoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5398446777381847464L;
	/**
	 * 员工月度考勤主键
	 */
	private String id;
	/**
	 * 员工月度考勤ID
	 */
	private String attenReportId;

	/**
	 * 出勤天数
	 */
	@JSONField(name = "cqts")
	private Double attendanceDays;
	/**
	 * 休息天数
	 */
	@JSONField(name = "xxts")
	private Double restDays;
	/**
	 * 工作时长
	 */
	@JSONField(name = "gzsc")
	private Double workHours;
	/**
	 * 迟到时长
	 */
	@JSONField(name = "cdsc")
	private Double lateHours;
	/**
	 * 迟到次数
	 */
	@JSONField(name = "cdcs")
	private Double lateTimes;
	/**
	 * 严重迟到时间
	 */
	@JSONField(name = "yzcdsc")
	private Double seriousLateHours;
	/**
	 * 严重迟到次数
	 */
	@JSONField(name = "yzcdcs")
	private Double seriousLateTimes;
	/**
	 * 早退时间
	 */
	@JSONField(name = "ztsc")
	private Double earlyLeaveHours;
	/**
	 * 早退次数
	 */
	@JSONField(name = "ztcs")
	private Double earlyLeaveTimes;
	/**
	 * 上班缺卡次数
	 */
	@JSONField(name = "sbqk")
	private Double workAbsenceTimes;
	/**
	 * 下班缺卡次数
	 */
	@JSONField(name = "xbqk")
	private Double offWorkAbsenceTimes;
	/**
	 * 一天未打卡天数
	 */
	@JSONField(name = "ytwdk")
	private Double unWorkDays;
	/**
	 * 旷工迟到天数
	 */
	@JSONField(name = "kgcd")
	private Double unWorkLateDays;
	/**
	 * 年月份
	 */
	@JSONField(name = "date")
	private Date monthTime;
	/**
	 * 外出天数 -- outDays
	 */
	@JSONField(name = "wcts")
	private Double outDays;
	/**
	 * 出差天数 -- busyAwayDays
	 */
	@JSONField(name = "ccts")
	private Double busyAwayDays;	
	/**
	 * 上传失败的理由
	 */
	private String failReason;
	/**
	 * 是否成功
	 */
	private Boolean isUploadSuccess;
	
	/**
	 * 员工的请假天数的字段集合
	 */
	private List<Integer> attenDays;
	
	/**
	 * 详细字段
	 */
    private List<SalCustomizedAttenFieldModel> details;
    
	/**
	 * 行号：在上传企业的薪资模板的时候用到
	 */
	private Integer rowNum;
	
	public SalStaffAttendanceModel() {
		super();
	}

	

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttenReportId() {
		return attenReportId;
	}

	public void setAttenReportId(String attenReportId) {
		this.attenReportId = attenReportId;
	}

	public Date getMonthTime() {
		return monthTime;
	}

	public void setMonthTime(Date monthTime) {
		this.monthTime = monthTime;
	}

	public List<SalCustomizedAttenFieldModel> getDetails() {
		return details;
	}

	public void setDetails(List<SalCustomizedAttenFieldModel> details) {
		this.details = details;
	}

    

	public Boolean getIsUploadSuccess() {
		return isUploadSuccess;
	}

	public void setIsUploadSuccess(Boolean isUploadSuccess) {
		this.isUploadSuccess = isUploadSuccess;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public List<Integer> getAttenDays() {
		return attenDays;
	}

	public void setAttenDays(List<Integer> attenDays) {
		this.attenDays = attenDays;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
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





	public Double getOutDays() {
		return outDays;
	}





	public void setOutDays(Double outDays) {
		this.outDays = outDays;
	}





	public Double getBusyAwayDays() {
		return busyAwayDays;
	}





	public void setBusyAwayDays(Double busyAwayDays) {
		this.busyAwayDays = busyAwayDays;
	}
	
	
}
