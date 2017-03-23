package me.ywork.suite.api.model;

public class CheckDataRpcModel {
	//唯一标示ID
	private String id;
	//考勤组ID
	private String groupId;
	//排班ID
	private String planId;
	//打卡记录ID
	private String recordId;
	//用户ID
	private String userId;
	//考勤类型（OnDuty：上班，OffDuty：下班）
	private String checkType;
	//时间结果（Normal:正常;Early:早退; Late:迟到;SeriousLate:严重迟到；NotSigned:未打卡）
	private String timeResult;
	//位置结果（Normal:范围内；Outside:范围外）
	private String locationResult;
	//工作日
	private Long workDate;
	//计算迟到和早退，基准时间
	private Long baseCheckTime;
	//实际打卡时间 -60
	private Long userCheckTime;
	
	//private String corpId;
	//关联的审批id
	private String approveId;
	//private String sourceType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getTimeResult() {
		return timeResult;
	}
	public void setTimeResult(String timeResult) {
		this.timeResult = timeResult;
	}
	public String getLocationResult() {
		return locationResult;
	}
	public void setLocationResult(String locationResult) {
		this.locationResult = locationResult;
	}
	public Long getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Long workDate) {
		this.workDate = workDate;
	}
	public Long getBaseCheckTime() {
		return baseCheckTime;
	}
	public void setBaseCheckTime(Long baseCheckTime) {
		this.baseCheckTime = baseCheckTime;
	}
	public Long getUserCheckTime() {
		return userCheckTime;
	}
	public void setUserCheckTime(Long userCheckTime) {
		this.userCheckTime = userCheckTime;
	}
	public String getApproveId() {
		return approveId;
	}
	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}
	
}
