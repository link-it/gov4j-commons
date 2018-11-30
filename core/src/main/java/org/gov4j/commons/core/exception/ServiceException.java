package org.gov4j.commons.core.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message, Throwable cause){
		super(message, cause);
	}

	public ServiceException(Throwable cause){
		super(cause);
	}

	public ServiceException() {
		super();
	}

	public ServiceException(String msg) {
		super(msg);
	}

}
