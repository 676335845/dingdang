package me.ywork.salarybill.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;


public class SalarySmsSendMode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6140063753714287868L;

	/**
	 * 操作人
	 */
	@JSONField(name = "nm")
	private String name;
	
	/**
	 * 操作时间
	 */
	@JSONField(name = "ct")
	private Date time;
	
	/**
	 * 短信消耗条数
	 */
	@JSONField(name = "fc")
	private int count;
	
	@JSONField(serialize = false)
	private int batchCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}
	
	
}
