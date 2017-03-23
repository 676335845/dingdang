package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalUsePageType   implements IStatusEnum<Short> {
	
	/**
	  * 需要分页
	 */
	UsePage((short) 1, "UsePage", "SalUsePageType.UsePage"),
	/**
	 * 不需要分页
	 */
	NotUsePage((short) 0,"NotUsePage","SalUsePageType.NotUsePage");
	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalUsePageType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalUsePageType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalUsePageType result = null;

		for (SalUsePageType status : SalUsePageType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in SalUsePageType");
		}

		return result;
	}
	

}
