package me.ywork.salarybill.entity;


import me.ywork.base.entity.Entity;

/**
 * 系统模板
 */
public class SalaryBillSystemTemplate extends Entity {

	private static final long serialVersionUID = -1581975887830680505L;

	/**
	 * 模板名称
	 */
	private String tempName;
	
	/**
	 * 模板顺序
	 * @return
	 */
	private Integer tempSort;

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public Integer getTempSort() {
		return tempSort;
	}

	public void setTempSort(Integer tempSort) {
		this.tempSort = tempSort;
	}


}
