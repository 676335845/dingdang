package com.heyi.framework.messagebus.message.responsible;

import com.heyi.framework.messagebus.message.IMessageHandler;

public interface ResponsibleMessageHandler extends IMessageHandler{
	/**
	 * 
	 * @param object
	 * @return
	 */
	Object onResponsibleMessage(Object object);
}
