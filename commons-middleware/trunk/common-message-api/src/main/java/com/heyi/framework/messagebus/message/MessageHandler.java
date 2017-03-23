package com.heyi.framework.messagebus.message;

public interface MessageHandler<M extends DefaultMessage> extends IMessageHandler {

	public void onMessage(M object);
}
