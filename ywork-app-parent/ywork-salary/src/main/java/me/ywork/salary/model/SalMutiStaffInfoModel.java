package me.ywork.salary.model;

import java.io.Serializable;
import java.util.List;

public class SalMutiStaffInfoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7995866920992029177L;
	/**
	 * 人员id的集合
	 */
	private List<String> staffIds;
	/**
	 * 时间戳
	 */
	private Long monthId;
	/**
	 * 钉钉企业号
	 */
	private String corpId;

	/**
	 * 月度工资报表的ID
	 */
	private String salReportId;

	/**
	 * 月度考勤报表的ID
	 */
	private String attendanceReportId;
	
	

	public SalMutiStaffInfoModel() {
		super();
	}

	public List<String> getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(List<String> staffIds) {
		this.staffIds = staffIds;
	}

	public Long getMonthId() {
		return monthId;
	}

	public void setMonthId(Long monthId) {
		this.monthId = monthId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getSalReportId() {
		return salReportId;
	}

	public void setSalReportId(String salReportId) {
		this.salReportId = salReportId;
	}

	public String getAttendanceReportId() {
		return attendanceReportId;
	}

	public void setAttendanceReportId(String attendanceReportId) {
		this.attendanceReportId = attendanceReportId;
	}

}
