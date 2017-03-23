package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

/**
 * 月度薪资表的状态：预估、等待审批、锁定
 *
 * Created by xiaobai on 2017/1/11.
 */
public enum SalReportStateType implements IStatusEnum<Short> {
	/**
	 * 预估(0)
	 */
	ESTIMATE((short) 0, "ESTIMATE", "SalaryReportStateType.ESTIMATE"),

	/**
	 * 等待审批(1)
	 */
	WAITING_APPROVAL((short) 1, "WAITING_APPROVAL", "SalaryReportStateType.WAITING_APPROVAL"),

	/**
	 * 锁定(2)
	 */
	LOCK((short) 2, "LOCK", "SalaryReportStateType.LOCK");
	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalReportStateType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalReportStateType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalReportStateType result = null;

		for (SalReportStateType status : SalReportStateType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalaryReportStateType");
		}

		return result;
	}
}
