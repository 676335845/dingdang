package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

/**
 * Created by xiaobai on 2017/1/11.
 */
public enum SalCalRuleType implements IStatusEnum<Short> {

	/**
	 * 薪资计算规则：正算(1)
	 */
	FRONT((short) 1, "FRONT", "SalaryCalRuleType.FRONT"),

	/**
	 * 薪资计算规则：反算(0)
	 */
	OPPOSITE((short) 0, "OPPOSITE", "SalaryCalRuleType.OPPOSITE");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalCalRuleType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalCalRuleType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalCalRuleType result = null;

		for (SalCalRuleType status : SalCalRuleType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalaryCalRuleType");
		}

		return result;
	}
}
