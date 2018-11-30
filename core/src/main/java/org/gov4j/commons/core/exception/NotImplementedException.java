package org.gov4j.commons.core.exception;

public class NotImplementedException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotImplementedException(String message, Throwable cause){
		super(message, cause);
	}

	public NotImplementedException(Throwable cause){
		super(cause);
	}

	public NotImplementedException() {
		super();
	}

	public NotImplementedException(String msg) {
		super(msg);
	}

}
