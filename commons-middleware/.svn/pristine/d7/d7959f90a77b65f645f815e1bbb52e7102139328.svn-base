package com.heyi.framework.messagebus.message.responsible.async;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;

/**
 * 
 * @author sulta
 *
 * @param <V>
 */
public class AsyncOperation<V> extends DefaultPromise<V>{
	
	protected Long createTimeStamp = System.currentTimeMillis(); /*创建的时间*/
	
	public AsyncOperation(EventExecutor executor) {
		super(executor);
	}

	public void addListener(AsyncOperationListner<V> listner) {
		super.addListener(listner);
	}

	public Long getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Long createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}
}
