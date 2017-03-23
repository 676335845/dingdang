package com.heyi.scheduler;

import com.heyi.scheduler.ITimedTaskKey;

public abstract class DelayedEventTaskKey implements ITimedTaskKey {

	public static final String SYSTEMNAME = "_epcEvent_";

	private String fdTaskChannel;
	
	public DelayedEventTaskKey(String channel) {
		this.fdTaskChannel = channel;
	}
	
	@Override
	public final String getFdSystemName() {
		return SYSTEMNAME;
	}

	public String getFdTaskChannel() {
		return fdTaskChannel;
	}

	public void setFdTaskChannel(String fdTaskChannel) {
		this.fdTaskChannel = fdTaskChannel;
	}
}
