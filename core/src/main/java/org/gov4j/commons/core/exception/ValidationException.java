package org.gov4j.commons.core.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message, Throwable cause){
		super(message, cause);
	}

	public ValidationException(Throwable cause){
		super(cause);
	}

	public ValidationException() {
		super();
	}

	public ValidationException(String msg) {
		super(msg);
	}

}
