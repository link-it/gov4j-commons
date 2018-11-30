package org.gov4j.commons.api.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.gov4j.commons.core.properties.Constants;
import org.gov4j.commons.core.properties.LocalPropertiesReader;
import org.gov4j.commons.core.properties.PropertiesException;
import org.slf4j.Logger;

public class ApplicationContext implements ServletContextListener{

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationContext.class);
	
	private static LocalPropertiesReader applicationConfig = null;
	public static LocalPropertiesReader getApplicationConfig() {
		return applicationConfig;
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		try{
			log.info("Inizializzazione ApplicationContext in corso...");
			
			// in futuro usare https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
			String projectHome = context.getServletContext().getInitParameter(Constants.PARAMETER_PROJECT_HOME);
			if(projectHome==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_PROJECT_HOME);
			}
			
			String applicationConfigName = context.getServletContext().getInitParameter(Constants.PARAMETER_APPLICATION_CONFIG);
			if(applicationConfigName==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_APPLICATION_CONFIG);
			}
			
			String applicationLocalConfigName = context.getServletContext().getInitParameter(Constants.PARAMETER_APPLICATION_LOCAL_CONFIG);
			if(applicationLocalConfigName==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_APPLICATION_LOCAL_CONFIG);
			}
			
			applicationConfig = new LocalPropertiesReader(projectHome, applicationConfigName, log);
			applicationConfig.setLocalFileImplementation(applicationLocalConfigName);
			
			log.info("Inizializzazione ApplicationContext completata");
		} catch(Exception e){
			String errorMsg = String.format("Inizializzazione ApplicationContext fallita: %s", e.getMessage());
			log.error(errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}

	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
}
