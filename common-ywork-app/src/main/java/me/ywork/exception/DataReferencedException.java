package me.ywork.exception;

/**
 * 数据被引用异常，通常被其它地方引用的数据不能被删除
 * @author TangGang  2015年7月27日
 *
 */
public class DataReferencedException extends BizException {
	private static final long serialVersionUID = -2061636893972063170L;

	/**
	 * 被引用的数据ID
	 */
	private final String id;

	public DataReferencedException(String id) {
		super();
		
		this.id = id;
	}

	public DataReferencedException(String id, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
		this.id = id;
	}

	public DataReferencedException(String id, String message, Throwable cause) {
		super(message, cause);
		
		this.id = id;
	}

	public DataReferencedException(String id, String message) {
		super(message);
		
		this.id = id;
	}

	public DataReferencedException(String id, Throwable cause) {
		super(cause);
		
		this.id = id;
	}

	public String getId() {
		return id;
	}	
	
}
