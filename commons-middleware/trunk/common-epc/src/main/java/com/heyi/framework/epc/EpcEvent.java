package com.heyi.framework.epc;

/**
 * EPC事件过程链-事件
 * @author sulta
 *
 */
public interface EpcEvent extends EpcObject{
	/**
	 * EPC事件编号
	 * @return
	 */
	String getEpcEventNo();
	
	/**
	 * 定时事件，指定时间触发
	 * @return
	 */
	Long getStartDeliverTime();
	
	void setEpcEventNo(String epcEventNo);
	/**
	 * 使用指定的ons tag发送 epc事件 (ONS模式下)，如果不指定则使用默认的tag
	 * @return
	 */
	String getEventMessageOnsTag();
	
	
	/**
	 * 使用指定的kafka topic发送事件(kafka模式下) ，如果不指定则使用默认的topic
	 * @return
	 */
	String getEventMessageKafkaTopic();	
}
