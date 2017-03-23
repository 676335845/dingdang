package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalExcelTimeType   implements IStatusEnum<Short>{

	DOUBLE((short) 0, "DOUBLETIME", "ExcelTimeType.DOUBLETIME"),


	HOURMINUTE((short) 1, "HOURMINUTE", "SalaryCalRuleType.HOURMINUTE"),
	
	HOUR((short) 2, "HOUR", "SalaryCalRuleType.HOUR"),
	DAY((short) 3, "DAY", "SalaryCalRuleType.DAY");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalExcelTimeType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalExcelTimeType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalExcelTimeType result = null;

		for (SalExcelTimeType status : SalExcelTimeType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in ExcelTimeType");
		}

		return result;
	}

}
