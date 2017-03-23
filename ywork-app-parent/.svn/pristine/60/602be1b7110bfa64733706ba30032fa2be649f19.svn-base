package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

/**
 * Created by xiaobai on 2017/1/11.
 */
public enum SalCorpPassStateType implements IStatusEnum<Short> {

	/**
	 * 企业密码锁开启(1)
	 */
	OPEN((short) 1, "OPEN", "CorpPassStateType.OPEN"),

	/**
	 * 企业密码锁关闭(0)
	 */
	CLOSE((short) 0, "CLOSE", "CorpPassStateType.CLOSE");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalCorpPassStateType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalCorpPassStateType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalCorpPassStateType result = null;

		for (SalCorpPassStateType status : SalCorpPassStateType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in CorpPassStateType");
		}

		return result;
	}
}
