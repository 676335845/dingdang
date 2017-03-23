package com.heyi.scheduler;

import com.heyi.message.timedtask.TimedTaskAlarmClock;

public interface Delegate {
	
	/**
	 * 是否支持此类别的消息
	 * @param taskCategory
	 * @return
	 */
	boolean supportsCategory(String taskCategory);
	
	/**
	 * 定时任务到达
	 * @param message
	 * @param jobParam
	 */
	void onTimedTaskEvent(TimedTaskAlarmClock message , Object jobParam);
}
