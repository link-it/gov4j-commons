package org.gov4j.commons.core.exception;

public class CoreException extends Exception {

	private static final long serialVersionUID = 1L;

	public CoreException(String message, Throwable cause){
		super(message, cause);
	}

	public CoreException(Throwable cause){
		super(cause);
	}

	public CoreException() {
		super();
	}

	public CoreException(String msg) {
		super(msg);
	}

}
