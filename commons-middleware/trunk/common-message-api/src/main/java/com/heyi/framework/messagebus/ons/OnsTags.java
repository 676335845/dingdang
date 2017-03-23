package com.heyi.framework.messagebus.ons;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class OnsTags {
	
	/**
	 * 线上订单
	 */
	public static final String Tag_OrderTrade = "orderTrade";
	
	/**
	 * 现场销售
	 */
	public static final String Tag_OfflineSale = "offlineSale";
	
	/**
	 * 支付网关
	 */
	public static final String Tag_Paygetway = "paygetway";
	
	/**
	 * 备货请求
	 */
	public static final String Tag_PrepareRequest = "preparerequest";
	
	/**
	 * 备货结果通知
	 */
	public static final String Tag_PrepareNotify = "preparenotify";
	
	/**
	 * Epc
	 */
	public static final String Tag_Epc = "epc";
	
	
	public static String tags(String ... tags) {
		Set<String> names = new HashSet<>();
		
		for (String tag : tags) {
			names.add(tag);
		}
		
		return StringUtils.join(names , " || ");
	}
}
