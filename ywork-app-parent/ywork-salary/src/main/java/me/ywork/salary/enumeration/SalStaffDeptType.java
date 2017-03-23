package me.ywork.salary.enumeration;

import me.ywork.enums.IStatusEnum;

public enum SalStaffDeptType  implements IStatusEnum<Short> {
	/**
	 *部门
	 */
	DEPT((short) 2, "DEPT", "StaffDeptType.DEPT"),

	/**
	 * 员工
	 */
	STAFF((short) 8, "STAFF", "StaffDeptType.STAFF"),
	
	/**
	 * 员工在某个部门下
	 */
	STAFFINDEPT((short) 64, "STAFFINDEPT", "StaffDeptType.STAFFINDEPT");
	
	

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;

	SalStaffDeptType(Short code, String defaultLabel, String resourceKey) {
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

	public static SalStaffDeptType valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - param code is null.");
		}

		SalStaffDeptType result = null;

		for (SalStaffDeptType status : SalStaffDeptType.values()) {
			if (status == null) {
				continue;
			}

			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("cannot find enum with code[" + code + "] in StaffDeptType");
		}

		return result;
	}

}
