package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalRuleType   implements IStatusEnum<Short>{
	/**
	 * 个人所得税
	 */
	PERSONALTAX((short) 2, "PERSONALTAX", "SalRuleType.PERSONALTAX"),

	/**
	 * 社保公积金
	 */
     SOCIAL((short) 1, "SOCIAL", "SalRuleType.SOCIAL"),
     
     /**
      * 基本薪资规则
      */
     BSRULE((short)0,"BSRULE","SalRuleTyoe.BSRULE");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalRuleType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalRuleType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalRuleType result = null;

		for (SalRuleType status : SalRuleType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalRuleType");
		}

		return result;
	}

}
