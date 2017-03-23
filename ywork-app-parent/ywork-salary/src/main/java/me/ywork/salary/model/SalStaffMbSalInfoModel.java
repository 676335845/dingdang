package me.ywork.salary.model;

import java.util.List;

public class SalStaffMbSalInfoModel {
	  /*
	   * 员工ID
	   */
	private String staffId;
	  /*
	   * 名字
	   */
	 private String name;
	  /*
	   * 头像
	   */
	 private String  avatar;
	  /*
	   *  部门
	   */
	 private String  deptName;
	  /*
	   *  应发工资
	   */
	 private Double  yfgz;
	  /*
	   *  实发工资
	   */
	 private Double  sfgz;
	  /*
	   *  代缴
	   */
	 private Double  dj;
	  /*
	   *  扣款
	   */
	 private Double kk;
	 /**
	  * 薪资的详细情况
	  */
	 private List<SalSysFieldItemModel> detail;
	 
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public Double getYfgz() {
		return yfgz;
	}
	public void setYfgz(Double yfgz) {
		this.yfgz = yfgz;
	}
	public Double getSfgz() {
		return sfgz;
	}
	public void setSfgz(Double sfgz) {
		this.sfgz = sfgz;
	}
	public Double getDj() {
		return dj;
	}
	public void setDj(Double dj) {
		this.dj = dj;
	}
	public Double getKk() {
		return kk;
	}
	public void setKk(Double kk) {
		this.kk = kk;
	}
	public List<SalSysFieldItemModel> getDetail() {
		return detail;
	}
	public void setDetail(List<SalSysFieldItemModel> detail) {
		this.detail = detail;
	}
}
