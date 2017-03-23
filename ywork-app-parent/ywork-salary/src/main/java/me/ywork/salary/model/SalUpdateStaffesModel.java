package me.ywork.salary.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
public class SalUpdateStaffesModel {
   
    @JSONField(name ="cbRuleId")
    private String corpBaseRuleId;
   
    /**
     * 规则类型，0是社保公积金 ，1是个人所得税
     */
    private Short ruleType;
    private List<SalStaffBaseInfoModel> staffInfos;

	public String getCorpBaseRuleId() {
		return corpBaseRuleId;
	}
	public void setCorpBaseRuleId(String corpBaseRuleId) {
		this.corpBaseRuleId = corpBaseRuleId;
	}
    
	public Short getRuleType() {
		return ruleType;
	}
	public void setRuleType(Short ruleType) {
		this.ruleType = ruleType;
	}
	public List<SalStaffBaseInfoModel> getStaffInfos() {
		return staffInfos;
	}
	public void setStaffInfos(List<SalStaffBaseInfoModel> staffInfos) {
		this.staffInfos = staffInfos;
	}
    
}
