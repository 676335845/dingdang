package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalLateEarlyDeductType implements IStatusEnum<Short>{
	/**
	 * 选择浮动款项的地址本的部门或人员
	 */
	Times((short) 0, "Times", "LateEarlyDeductType.Times"),

	/**
	 * 选择需要重置的员工或者部门的
	 */
	Duration((short) 1, "Duration", "LateEarlyDeductType.Duration");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalLateEarlyDeductType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalLateEarlyDeductType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalLateEarlyDeductType result = null;

		for (SalLateEarlyDeductType status : SalLateEarlyDeductType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in LateEarlyDeductType");
		}

		return result;
	}

}
