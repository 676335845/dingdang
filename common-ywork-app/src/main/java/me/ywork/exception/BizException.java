package me.ywork.exception;

/**
 * 具有通知状态的异常，从此类继承的子类通常是检查异常，需要客户端显示处理
 * @author TangGang  2015年7月27日
 *
 */
public abstract class BizException extends Exception {
	private static final long serialVersionUID = 7533466100552046484L;

	public BizException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BizException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BizException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BizException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
