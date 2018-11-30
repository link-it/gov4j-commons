package org.gov4j.commons.core.exception;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message, Throwable cause){
		super(message, cause);
	}

	public NotFoundException(Throwable cause){
		super(cause);
	}

	public NotFoundException() {
		super();
	}

	public NotFoundException(String msg) {
		super(msg);
	}

}
