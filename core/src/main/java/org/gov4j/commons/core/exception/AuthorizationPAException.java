package org.gov4j.commons.core.exception;

public class AuthorizationPAException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthorizationPAException(String message, Throwable cause){
		super(message, cause);
	}

	public AuthorizationPAException(Throwable cause){
		super(cause);
	}

	public AuthorizationPAException() {
		super();
	}

	public AuthorizationPAException(String msg) {
		super(msg);
	}

}
