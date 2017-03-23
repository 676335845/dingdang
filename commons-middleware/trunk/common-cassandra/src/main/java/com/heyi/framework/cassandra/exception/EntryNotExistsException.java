package com.heyi.framework.cassandra.exception;

public class EntryNotExistsException extends DBException {
	private static final long serialVersionUID = 1L;

	// ~--- constructors
	// ---------------------------------------------------------

	/**
	 * Creates a new <code>EntryNotExistsException</code> instance.
	 * 
	 * @param message
	 */
	public EntryNotExistsException(String message) {
		super(message);
	}

	/**
	 * Creates a new <code>EntryNotExistsException</code> instance.
	 * 
	 * @param message
	 * @param cause
	 */
	public EntryNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}