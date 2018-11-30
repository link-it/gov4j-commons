package org.gov4j.commons.api.impl;

import org.gov4j.commons.core.logger.AbstractLogger;
import org.slf4j.Logger;

public class ServiceLogger extends AbstractLogger {

	private String idOperazione;
	private String methodName;
	private String prefix;
	
	public ServiceLogger(String idOperazione, String methodName, String className, Logger log) {
		super(log);
		this.idOperazione = idOperazione;
		this.methodName = methodName;
		String classe = className;
		if(className.contains(".")) {
			String [] tmp = className.split("\\.");
			classe = tmp[tmp.length-1];
		}
		this.prefix = String.format("<%s> (%s.%s) ", this.idOperazione,classe,this.methodName);
	}
	
	@Override
	protected String appendPrefix(String message) {
		StringBuilder bf = new StringBuilder(this.prefix);
		bf.append(message);
		return bf.toString();
	}
	
}
