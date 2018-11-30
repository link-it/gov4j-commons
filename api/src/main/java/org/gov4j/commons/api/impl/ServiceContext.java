package org.gov4j.commons.api.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceContext {

	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private UriInfo uriInfo;
	private String idOperazione;
	private String className;
	private String methodName;
	private ServiceLogger logger;
	private Authentication authentication;
	private String restPath;
	
	public ServiceContext(HttpServletRequest servletRequest, HttpServletResponse servletResponse, UriInfo uriInfo, int level, Logger log) {
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
		this.uriInfo = uriInfo;
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		if(stackTrace!=null && stackTrace.length>=(level+1)) {
			this.className = stackTrace[level].getClassName();
			this.methodName = stackTrace[level].getMethodName();
		}
		this.idOperazione = UUID.randomUUID().toString();
		this.logger = new ServiceLogger(this.idOperazione, this.methodName, this.className, log);
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
	}
	
	public HttpServletRequest getServletRequest() {
		return this.servletRequest;
	}
	public UriInfo getUriInfo() {
		return this.uriInfo;
	}
	public String getIdOperazione() {
		return this.idOperazione;
	}
	public String getClassName() {
		return this.className;
	}
	public String getMethodName() {
		return this.methodName;
	}
	public ServiceLogger getLogger() {
		return this.logger;
	}
	public Authentication getAuthentication() {
		return this.authentication;
	}
	public String getRestPath() {
		return this.restPath;
	}
	void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	public HttpServletResponse getServletResponse() {
		return this.servletResponse;
	}
	
}
