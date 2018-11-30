package org.gov4j.commons.api.impl;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.gov4j.commons.api.listener.ApplicationContext;
import org.gov4j.commons.core.properties.LocalPropertiesReader;
import org.slf4j.Logger;

public class BaseImpl {

	@Context 
	protected HttpServletRequest servletRequest;
	@Context 
	protected HttpServletResponse servletResponse;
	@Context
	protected UriInfo uriInfo;
	
	protected Logger log;
	
	protected LocalPropertiesReader appProperties;
	
	public BaseImpl(Logger log){
		this.log = log;
		this.appProperties = ApplicationContext.getApplicationConfig();
	}
	
	protected synchronized ServiceContext getContext() {
		ServiceContext context = new ServiceContext(this.servletRequest, this.servletResponse, this.uriInfo, 2, this.log);
		context.setRestPath(this.getPathFromRestMethod(context.getMethodName()));
		return context;
	}
	
	protected LocalPropertiesReader getAppProperties() {
		return this.appProperties;
	}
	
	private String getPathFromRestMethod(String methodName) {

        try {
        	Class<?> c = this.getClass();
        	Class<?> [] interfaces = c.getInterfaces();
        	if(interfaces==null || interfaces.length<=0) {
        		return null;
        	}
        	Class<?> cInterface = null;
        	for (int i = 0; i < interfaces.length; i++) {
        		if (interfaces[i] != null && interfaces[i].isAnnotationPresent(Path.class)) {
        			cInterface = interfaces[i];
        			break;
        		}
			}
        	if(cInterface==null) {
        		return null;
        	}
        	Method [] methods = cInterface.getMethods();
        	if(methods==null || methods.length<=0) {
        		return null;
        	}
        	Method method = null;
        	for (int i = 0; i < methods.length; i++) {
        		if (methods[i] != null && methods[i].getName().equals(methodName) && methods[i].isAnnotationPresent(Path.class)) {
        			method = methods[i];
        			break;
        		}
			}
        	if(method==null) {
        		return null;
        	}
        	Path path = method.getAnnotation(Path.class);
        	if(path==null) {
        		return null;
        	}
        	return path.value();
        } catch (Exception e) {
            this.log.error(e.getMessage(),e);
        }

        return null;
    }
	
}
