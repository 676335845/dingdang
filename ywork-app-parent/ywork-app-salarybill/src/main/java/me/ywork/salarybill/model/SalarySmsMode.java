package me.ywork.salarybill.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


public class SalarySmsMode implements Serializable {

	/**
	 * 短信发送规则
	 */
	@JSONField(name = "sm")
	private Short smsMode;
	
	/**
	 * 显示规则
	 */
	@JSONField(name = "ss")
	private Short smsShow;
	
	/**
	 * 没有手机的员工数量
	 */
	@JSONField(name = "nmu")
	private int noMobleUser;
	
	/**
	 * 剩余短信
	 */
	@JSONField(name = "ft")
	private int freeTotal;
	
	/**
	 * 使用短信
	 */
	@JSONField(name = "uc")
	private int usedCount;
	
	/**
	 * 节省钱
	 */
	@JSONField(name = "smy")
	private String saveMoney;
	
	/**
	 * 短信发送记录
	 */
	@JSONField(name = "ssm")
	private List<SalarySmsSendMode> salarySmsSendMode;

	
	public List<SalarySmsSendMode> getSalarySmsSendMode() {
		return salarySmsSendMode;
	}

	public void setSalarySmsSendMode(List<SalarySmsSendMode> salarySmsSendMode) {
		this.salarySmsSendMode = salarySmsSendMode;
	}

	public Short getSmsMode() {
		return smsMode;
	}

	public void setSmsMode(Short smsMode) {
		this.smsMode = smsMode;
	}

	public Short getSmsShow() {
		return smsShow;
	}

	public void setSmsShow(Short smsShow) {
		this.smsShow = smsShow;
	}

	public int getNoMobleUser() {
		return noMobleUser;
	}

	public void setNoMobleUser(int noMobleUser) {
		this.noMobleUser = noMobleUser;
	}

	public int getFreeTotal() {
		return freeTotal;
	}

	public void setFreeTotal(int freeTotal) {
		this.freeTotal = freeTotal;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public String getSaveMoney() {
		return saveMoney;
	}

	public void setSaveMoney(String saveMoney) {
		this.saveMoney = saveMoney;
	}
	
	
}
