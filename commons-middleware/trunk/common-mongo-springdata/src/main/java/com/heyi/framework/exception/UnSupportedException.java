package com.heyi.framework.exception;

/**
 * 未支持异常
 * @author
 *
 */
public class UnSupportedException extends RuntimeException {
	private static final long serialVersionUID = 5838263333548849451L;

	public UnSupportedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnSupportedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UnSupportedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnSupportedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnSupportedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
