package me.ywork.salarybill.base;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import me.ywork.enums.IStatusEnum;

/**
 * 模板类型
 * @author lizh  2015年10月26日
 *
 */
public enum SalaryTemplateType implements IStatusEnum<Short> {
	 /**
	 * 默认
	 */
	Default((short)2, "默认模板", "SalaryTemplateType.Default"),
	
	 /**
	 * 工资条
	 */
	Salary((short)1, "工资条模板", "SalaryTemplateType.Salary")
//	,
//	/**
//	 * 考勤
//	 */
//	check((short)2, "考勤模板", "SalaryTemplateType.Check")
	 
	;
	
	public static void main(String[] args) {
		JSONObject jo = (JSONObject) JSONObject.toJSON(SalaryTemplateType.getAllType());
		System.out.println(jo.toJSONString());
	}

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;
	
	public static Map<Short,String> getAllType(){
		Map<Short,String> templateTypeMap = new HashMap<Short,String>();
		for (SalaryTemplateType s : SalaryTemplateType.values()) {
			templateTypeMap.put(s.code, s.defaultLabel);
		}
		return templateTypeMap;
	}
	

	private SalaryTemplateType(Short code, String defaultLabel, String resourceKey) {
		this.code = code;
		this.defaultLabel = defaultLabel;
		this.resourceKey = resourceKey;
	}

	@Override
	public Short getCode() {
		return code;
	}

	@Override
	public String getDefaultLabel() {
		return defaultLabel;
	}

	@Override
	public String getResourceKey() {
		return resourceKey;
	}
	
	public static SalaryTemplateType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - parameter code is null.");
		}
		
		SalaryTemplateType result = null;
		
		for (SalaryTemplateType voteStatus : SalaryTemplateType.values()) {
			if (voteStatus == null) {
				continue;
			}
			
			if (voteStatus.getCode().equals(code)) {
				result = voteStatus;
				break;
			}
		}
		
		if (result == null) {
			throw new IllegalStateException("valueOf - cannot find the enum item with code:" + code);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return new StringBuilder(super.toString())
		        .append("[")
		        .append("code:").append(this.getCode())
		        .append(", defaultLabel:").append(this.getDefaultLabel())
		        .append("]")
				.toString();
	}
	
}
