package org.gov4j.commons.core.exception;

public class AuthorizationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message, Throwable cause){
		super(message, cause);
	}

	public AuthorizationException(Throwable cause){
		super(cause);
	}

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String msg) {
		super(msg);
	}

}
