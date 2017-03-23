package com.heyi.framework.messagebus.message;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.heyi.framework.messagebus.message.commitable.CommitableMessageHandler;
import com.heyi.framework.messagebus.message.responsible.ResponsibleMessageHandler;

public class MessageHandlers<T extends IMessageHandler> implements Iterable<T>{
			
	private final Set<T> handlers = new CopyOnWriteArraySet<T>();

	/**
	 * 
	 */
	public static final MessageHandlers<MessageHandler<DefaultMessage>> DEFAULTHANDLERS = new MessageHandlers<MessageHandler<DefaultMessage>>();
	
	/**
	 * 可应答消息处理
	 */
	public static final MessageHandlers<ResponsibleMessageHandler> RESPHANDLERS = new MessageHandlers<ResponsibleMessageHandler>();
	
	/**
	 * 
	 */
	public static final MessageHandlers<CommitableMessageHandler> COMMITABLEHANDLERS = new MessageHandlers<CommitableMessageHandler>();

	
	@SuppressWarnings("unchecked")
	public static void addHandler(IMessageHandler handler) {
		if (ResponsibleMessageHandler.class.isAssignableFrom(handler.getClass())) {
			RESPHANDLERS.handlers.add((ResponsibleMessageHandler) handler);
		}		
		if (MessageHandler.class.isAssignableFrom(handler.getClass())) {
			DEFAULTHANDLERS.handlers.add((MessageHandler<DefaultMessage>) handler);
		}
		if (CommitableMessageHandler.class.isAssignableFrom(handler.getClass())) {
			COMMITABLEHANDLERS.handlers.add((CommitableMessageHandler) handler);
		}
    }
	
	public static void removeHandler(IMessageHandler handler) {
		if (ResponsibleMessageHandler.class.isAssignableFrom(handler.getClass())) {
			RESPHANDLERS.handlers.remove(handler);
		}
		if (MessageHandler.class.isAssignableFrom(handler.getClass())) {
			DEFAULTHANDLERS.handlers.remove(handler);
		}
		if (CommitableMessageHandler.class.isAssignableFrom(handler.getClass())) {
			COMMITABLEHANDLERS.handlers.remove(handler);
		}
    }
	
	@Override
	public Iterator<T> iterator() {
		return handlers.iterator();
	}

	public boolean isEmpty() {
		return handlers.isEmpty();
	}
}
