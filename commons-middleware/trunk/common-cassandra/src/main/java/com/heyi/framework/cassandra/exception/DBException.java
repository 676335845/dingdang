package com.heyi.framework.cassandra.exception;

public class DBException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>DBException</code> instance.
	 * 
	 */
	public DBException(String message) {
		super(message);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

} 
