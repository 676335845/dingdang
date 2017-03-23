package com.heyi.framework.epc;

/**
 * EPC事件过程链-功能
 * @author sulta
 *
 */
public interface EpcFunction<E extends EpcEvent> extends EpcObject{

	void execute(EpcContext<E> context);
	
	void nextEvent(EpcContext<E> context);
	
	boolean supports(E e);
}
