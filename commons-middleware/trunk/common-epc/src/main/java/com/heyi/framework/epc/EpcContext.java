package com.heyi.framework.epc;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行功能时用到的上下文
 * @author sulta
 *
 */
public class EpcContext <T extends EpcEvent> {
	
	protected final T source;
	
	protected final EpcServiceManager serviceManager;
	
	protected boolean isFireNextEvent = true; 
	
	protected final Map<String, Object>  tempData = new HashMap<>();
	
	public EpcContext(T event , EpcServiceManager service) {
		this.source = event;
		this.serviceManager = service;
	}

	public T getSource() {
		return source;
	}

	public EpcServiceManager getServiceManager() {
		return serviceManager;
	}
	
	public boolean isFireNextEvent() {
		return isFireNextEvent;
	}

	public void setFireNextEvent(boolean isFireNextEvent) {
		this.isFireNextEvent = isFireNextEvent;
	}

	public Map<String, Object> getTempData() {
		return tempData;
	}
	
	public void reset() {
		this.isFireNextEvent = true;
		tempData.clear(); 
	}
}
