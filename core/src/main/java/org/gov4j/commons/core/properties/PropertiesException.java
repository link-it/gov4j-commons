package org.gov4j.commons.core.properties;

public class PropertiesException extends Exception {

	private static final long serialVersionUID = 1L;

	public PropertiesException(String message, Throwable cause){
		super(message, cause);
	}

	public PropertiesException(Throwable cause){
		super(cause);
	}

	public PropertiesException() {
		super();
	}

	public PropertiesException(String msg) {
		super(msg);
	}

	public static void throwNotFoundInitParameter(String name) throws PropertiesException {
		throw new PropertiesException(new StringBuilder().append("InitServletParameter [").append(name).append("] non trovato").toString());
	}
}
