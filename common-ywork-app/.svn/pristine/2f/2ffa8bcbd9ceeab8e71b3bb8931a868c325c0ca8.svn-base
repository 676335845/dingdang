package me.ywork.result;

import com.heyi.utils.NumberUtils;

import me.ywork.enums.IStatusEnum;

/**
 * 结果状态
 * 
 * @author TangGang   2015-6-17
 *
 */
public enum ResultStatus implements IStatusEnum<Integer> {
	/**
	 * 权限不足(-2)
	 */
	PermissionDenied(-2, "权限不足", "ResultStatus.PermissionDenied"),
	/**
	 * 失败(-1)
	 */
	FAIL(-1, "失败", "ResultStatus.FAIL"),
	/**
	 * 成功(0)
	 */
	SUCCESS(0, "成功", "ResultStatus.SUCCESS"),
	/**
	 * 依赖其它操作(1)
	 */
	DEPENDCE(1, "依赖其它操作", "ResultStatus.DEPENDCE");

	private final Integer code;
	private final String  defaultLabel;
	private final String  resourceKey;
	
		
	private ResultStatus(Integer code, String defaultLabel, String resourceKey) {
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
	
	public static  ResultStatus valueOf(Integer code) {
		if (NumberUtils.isNull(code)) {
			throw new NullPointerException(
					"RefundStatus.valueOf - parameter code is null.");
		}
		
		ResultStatus result = null;
		
		for (ResultStatus status : ResultStatus.values()) {
			if (status == null) {
				continue;
			}
			
			if (status.getCode().equals(code)) {
				result = status;
				break;
			}
		}
		
		if (result == null) {
			throw new IllegalStateException("cannot find enum with code["
					+ code + "] in ResultStatus");
		}
		
		return result;
	}

}
