package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalDetailShowType implements IStatusEnum<Short> {
	/**
	 * 基本的薪资类型
	 */
	 BasicSalType((short) 0, "BasicSalType", "SalDetailShowType.BasicSalType"),

	/**
	 * 月度薪资报表的类型
	 */
	MonthSalType((short) 1, "MonthSalType", "SalDetailShowType.MonthSalType");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalDetailShowType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalDetailShowType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalDetailShowType result = null;

		for (SalDetailShowType status : SalDetailShowType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalDetailShowType");
		}

		return result;
	}
}
