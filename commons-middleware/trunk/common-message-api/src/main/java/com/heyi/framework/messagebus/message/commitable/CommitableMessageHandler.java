package com.heyi.framework.messagebus.message.commitable;

import com.heyi.framework.messagebus.message.IMessageHandler;

public interface CommitableMessageHandler extends IMessageHandler{
	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	boolean onCommitableMessage(ICommitableMessage object) throws Exception;
}
