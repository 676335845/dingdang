package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalReturnType  implements IStatusEnum<Integer> {
	
	/**
	  * 日期不正确
	 */
	EXCEL_INCORRECT((Integer) 1001, "EXCEL_INCORRECT", "SalReturnType.EXCEL_INCORRECT"),
	/**
	 * 参数不规范
	 */
	PARAMETER_INCORRECT((Integer) 1002,"PARAMETER_INCORRECT","SalReturnType.PARAMETER_INCORRECT");



	private final Integer code;
	private final String defaultLabel;
	private final String resourceKey;

	SalReturnType(Integer code, String defaultLabel, String resourceKey) {
		this.code = code;
		this.defaultLabel = defaultLabel;
		this.resourceKey = resourceKey;
	}

	@Override
	public Integer getCode() {
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

	public static SalReturnType valueOf(Integer code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalReturnType result = null;

		for (SalReturnType status : SalReturnType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalReturnType");
		}

		return result;
	}
}
