package com.heyi.framework.messagebus.service;

import org.springframework.context.ApplicationEvent;

public class ConsumerRefreshedEvent extends ApplicationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6704883703805885599L;

	public ConsumerRefreshedEvent(Object source) {
		super(source);
	}
}
