package me.ywork.salary.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 薪资规则的 Created by xiaobai on 2017/1/11.
 */
public class SalSysRuleModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4590234558264968648L;

	/**
	 * 获取规则ID
	 */
	private String id;

	/**
	 * 薪资规则描述
	 */
	@JSONField(name = "desc")
	private String salRuleDes;

	/**
	 * 薪资规则的名字
	 */
	@JSONField(name = "name")
	private String salRuleName;

	/**
	 * 薪资规则的类型
	 */
	@JSONField(name = "ruleType")
	private String salRuleType;
	
   /**
    * 创建时间
    */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date modifiedDate;
	

	public SalSysRuleModel() {
		super();
	}

	public String getSalRuleDes() {
		return salRuleDes;
	}

	public void setSalRuleDes(String salRuleDes) {
		this.salRuleDes = salRuleDes;
	}

	public String getSalRuleName() {
		return salRuleName;
	}

	public void setSalRuleName(String salRuleName) {
		this.salRuleName = salRuleName;
	}

	public String getSalRuleType() {
		return salRuleType;
	}

	public void setSalRuleType(String salRuleType) {
		this.salRuleType = salRuleType;
	}

   

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
	
	

}
