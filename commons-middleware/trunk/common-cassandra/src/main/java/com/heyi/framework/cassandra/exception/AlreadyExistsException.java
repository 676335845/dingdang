package com.heyi.framework.cassandra.exception;

public class AlreadyExistsException extends DBException {
	private static final long serialVersionUID = 1L;

	// ~--- constructors
	// ---------------------------------------------------------

	/**
	 * Creates a new <code>AlreadyExistsException</code> instance.
	 * 
	 * @param message
	 */
	public AlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * Creates a new <code>AlreadyExistsException</code> instance.
	 * 
	 * @param message
	 * @param cause
	 */
	public AlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}