package me.ywork.salarybill.base;

import java.util.HashMap;
import java.util.Map;

import me.ywork.enums.IStatusEnum;

/**
 * 短信模式
 * @author Think
 *
 */
public enum SalarySmsModeEnum implements IStatusEnum<Short> {
	 /**
	 * 默认钉钉，未读短信
	 */
	SMS_SEND_Default((short)1, "默认钉钉，未读短信", "SalarySmsMode.send.Default"),
	
	 /**
	 * 所有人钉钉和短信都通知
	 */
	SMS_SEND_ALL((short)2, "所有人钉钉和短信都通知", "SalarySmsMode.send.ALL")	,
	
	 /**
	 * 所有人钉钉和短信都通知
	 */
	SMS_SEND_NOACTIVITY((short)3, "未激活发送短信", "SalarySmsMode.send.NOACTIVITY")	,



	
	 
	/**
	 * 所有人钉钉和短信都通知
	 */
	SMS_SHOW_SHORT((short)1, "所有人钉钉和短信都通知", "SalarySmsMode.show.SHORT")	,
	
	/**
	 * 所有人钉钉和短信都通知
	 */
	SMS_SHOW_ALL((short)2, "所有人钉钉和短信都通知", "SalarySmsMode.show.ALL")
	;
	

	private final Short code;
	private final String defaultLabel;
	private final String resourceKey;
	
	public static Map<Short,String> getAllType(){
		Map<Short,String> templateTypeMap = new HashMap<Short,String>();
		for (SalarySmsModeEnum s : SalarySmsModeEnum.values()) {
			templateTypeMap.put(s.code, s.defaultLabel);
		}
		return templateTypeMap;
	}
	

	private SalarySmsModeEnum(Short code, String defaultLabel, String resourceKey) {
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
	
	public static SalarySmsModeEnum valueOf(Short code) {
		if (code == null) {
			throw new NullPointerException("valueOf - parameter code is null.");
		}
		
		SalarySmsModeEnum result = null;
		
		for (SalarySmsModeEnum voteStatus : SalarySmsModeEnum.values()) {
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
