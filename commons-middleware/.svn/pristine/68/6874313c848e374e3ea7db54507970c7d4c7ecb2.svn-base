package com.heyi.framework.messagebus.message.responsible;

/**
 * 
 * @author sulta
 *
 */
public class DefaultResponseFaultMessage extends DefaultResponseMessage{

	private static final long serialVersionUID = 8871161360644148196L;
	
	protected Throwable cause;

	public Throwable getCause() {
		return cause;
	}
	
	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public DefaultResponseFaultMessage() {
		super();
	}
	
	@Override
	public String toString() {
		return "DefaultResponseFaultMessage [type=" + type + ", cause= " + cause.getClass() + " | " + cause.getMessage() + "]";
	}
}
