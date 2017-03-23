package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalOptPersonType  implements IStatusEnum<Short> {
	/**
	 * 选择浮动款项的地址本的部门或人员
	 */
	FLOAT((short) 0, "FLOAT", "SalaryCalRuleType.FLOAT"),

	/**
	 * 选择需要重置的员工或者部门的
	 */
	RESET((short) 1, "RESET", "SalaryCalRuleType.RESET");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalOptPersonType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalOptPersonType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalOptPersonType result = null;

		for (SalOptPersonType status : SalOptPersonType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in OptPersonType");
		}

		return result;
	}
}
