package me.ywork.salary.entity;

import me.ywork.base.entity.Entity;

public class SalSysRuleEntity  extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6455265371573581875L;

	/**
	 * 薪资规则描述
	 */
	private String salRuleDes;

	/**
	 * 薪资规则的名字
	 */
	private String salRuleName;

	/**
	 * 薪资规则的类型
	 */
	private String salRuleType;
	
	

	public SalSysRuleEntity() {
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
	
	
}
