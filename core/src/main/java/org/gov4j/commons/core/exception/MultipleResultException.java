package org.gov4j.commons.core.exception;

public class MultipleResultException extends Exception {

	private static final long serialVersionUID = 1L;

	public MultipleResultException(String message, Throwable cause){
		super(message, cause);
	}

	public MultipleResultException(Throwable cause){
		super(cause);
	}

	public MultipleResultException() {
		super();
	}

	public MultipleResultException(String msg) {
		super(msg);
	}

}
