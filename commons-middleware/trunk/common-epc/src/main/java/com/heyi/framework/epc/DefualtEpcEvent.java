package com.heyi.framework.epc;

public abstract class DefualtEpcEvent implements EpcEvent {
	
	/**
	 * 定时事件，在指定的时间发送
	 */
	protected Long startDeliverTime;

	public Long getStartDeliverTime() {
		return startDeliverTime;
	}

	public void setStartDeliverTime(Long startDeliverTime) {
		this.startDeliverTime = startDeliverTime;
	}
	
	@Override
	public String getEventMessageOnsTag() {
		return null;
	}

	@Override
	public String getEventMessageKafkaTopic() {
		return null;
	}

}
