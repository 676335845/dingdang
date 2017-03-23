package me.ywork.salarybill.base;

import me.ywork.enums.IStatusEnum;

/**
 * 密码类型
 * @author lizh  2015年10月26日
 *
 */
public enum SalaryPwdType implements IStatusEnum<Short> {
	 /**
	 * 后台管理密码
	 */
	Manager((short)0, "后台管理密码", "SalaryPwdType.Manager"),
	 
	 /**
	 * 用户密码
	 */
	User((short)1, "用户密码", "SalaryPwdType.User");

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;
	

	private SalaryPwdType(Short code, String defaultLabel, String resourceKey) {
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
	
	public static SalaryPwdType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - parameter code is null.");
		}
		
		SalaryPwdType result = null;
		
		for (SalaryPwdType voteStatus : SalaryPwdType.values()) {
			if (voteStatus == null) {
				continue;
			}
			
			if (voteStatus.getCode().equals(code)) {
				result = voteStatus;
				break;
			}
		}
		
		if (result == null) {
			throw new IllegalStateException("valueOf - cannot find the enum item with code:" + code);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return new StringBuilder(super.toString())
		        .append("[")
		        .append("code:").append(this.getCode())
		        .append(", defaultLabel:").append(this.getDefaultLabel())
		        .append("]")
				.toString();
	}
	
}
