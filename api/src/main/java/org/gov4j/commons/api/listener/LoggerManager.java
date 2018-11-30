package org.gov4j.commons.api.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.gov4j.commons.core.properties.Constants;
import org.gov4j.commons.core.properties.LocalPropertiesReader;
import org.gov4j.commons.core.properties.PropertiesException;
import org.slf4j.Logger;

public class LoggerManager implements ServletContextListener{

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoggerManager.class);
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		try{
			log.info("Inizializzazione LoggerManager in corso...");
			
			String projectHome = context.getServletContext().getInitParameter(Constants.PARAMETER_PROJECT_HOME);
			if(projectHome==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_PROJECT_HOME);
			}
			
			String loggerConfigName = context.getServletContext().getInitParameter(Constants.PARAMETER_LOGGER_CONFIG);
			if(loggerConfigName==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_LOGGER_CONFIG);
			}
			
			String loggerLocalConfigName = context.getServletContext().getInitParameter(Constants.PARAMETER_LOGGER_LOCAL_CONFIG);
			if(loggerLocalConfigName==null) {
				PropertiesException.throwNotFoundInitParameter(Constants.PARAMETER_LOGGER_LOCAL_CONFIG);
			}
			
			LocalPropertiesReader lProperties = new LocalPropertiesReader(projectHome, loggerConfigName, log);
			lProperties.setLocalFileImplementation(loggerLocalConfigName, 
					ApplicationContext.getApplicationConfig().getProperty(Constants.PROPERTIES_CONF_DIR, false));
			
			Properties pLogger = new Properties();
			Enumeration<?> names = lProperties.propertyNames();
			while (names.hasMoreElements()) {
				String key = (String) names.nextElement();
				String value = lProperties.getProperty(key, false);
				if(value!=null) {
					pLogger.put(key, value);
				}
			}
			
			File fTmp = File.createTempFile("opensiope_log", ".properties");
			try (FileOutputStream foutTmp = new FileOutputStream(fTmp)){
				pLogger.store(foutTmp, "Tmp Configuration");
				foutTmp.flush();
			}
			org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			loggerContext.setConfigLocation(fTmp.toURI());
			if(fTmp.delete()==false) {
				String errorMsg = new StringBuilder().append("Eliminazione fallita del file temporaneo [").append(fTmp.getAbsolutePath()).append("]").toString();
				throw new Exception(errorMsg);
			}
			
			log.info("Inizializzazione LoggerManager completata");
		} catch(Exception e){
			String errorMsg = String.format("Inizializzazione LoggerManager fallita: %s", e.getMessage());
			log.error(errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}

	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
}
