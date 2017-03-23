package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalDeductType implements IStatusEnum<Short> {
	/**
	 * 日薪的百分比
	 */
	PERCENT((short) 1, "PERCENT", "DeductType.PERCENT"),

	/**
	 * 固定金额
	 */
	FIXED((short) 0, "FIXED", "DeductType.FIXED");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalDeductType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalDeductType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalDeductType result = null;

		for (SalDeductType status : SalDeductType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in DeductType");
		}

		return result;
	}
}
