package org.gov4j.commons.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;


public class PropertiesUtilities {

	private PropertiesUtilities() {}
	
	/*
	 * // RICERCA
		// 1. VARIABILE DI SISTEMA: PROJECT_HOME dove deve essere specificata una directory in cui verra' cercato il file path
		// 2. PROPRIETA' JAVA (es. ApplicationServer o Java con -D): PROJECT_HOME dove deve essere specificata una directory in cui verra' cercato il file path
		// 3. CLASSPATH con nome path
		// 4. (DIRECTORY DI CONFIGURAZIONE)/path
	*/
	public static CollectionProperties searchLocalImplementation(String projectHome, Logger log,String localFileName, String confDirectory){	
		
		CollectionProperties cp = new CollectionProperties();
		
		Properties p1 = PropertiesUtilities.examineStep1(projectHome,log,localFileName);
		if(p1!=null){
			cp.setSystemProjectHome(new PropertiesReader(p1));
		}
		
		Properties p2 = PropertiesUtilities.examineStep2(projectHome,log,localFileName);
		if(p2!=null){
			cp.setJavaProjectHome(new PropertiesReader(p2));
		}
			
		Properties p3 = PropertiesUtilities.examineStep3(log,localFileName);
		if(p3!=null){
			cp.setClasspath(new PropertiesReader(p3));
		}
				
		File fConfDirectory = null;
		if(confDirectory!=null){
			fConfDirectory = new File(confDirectory);
		}
		if(fConfDirectory!=null && fConfDirectory.exists() && fConfDirectory.isDirectory() ){
			Properties p4 = PropertiesUtilities.examineStep4(log,localFileName,fConfDirectory);
			if(p4!=null){
				cp.setConfigDir(new PropertiesReader(p4));
			}
		}
			
		return cp;
	}

	private static Properties examineStep1(String projectHome,Logger log,String path){
		String dir = System.getenv(projectHome);
		String subject = new StringBuilder().append("[").append("Variabile di sistema ").append(projectHome).append("] ").toString();
		if(dir!=null){
			File fDir = new File(dir);
			if(!fDir.exists()){
				String errorMsg = new StringBuilder().append(subject).append("Directory non esistente: ").append(fDir.getAbsolutePath()).toString();
				log.error(errorMsg);
				return null;
			}
			if(!fDir.canRead()){
				String errorMsg = new StringBuilder().append(subject).append("Directory non accessibile: ").append(fDir.getAbsolutePath()).toString();
				log.error(errorMsg);
				return null;
			}
			return PropertiesUtilities.getPropertiesReader(log,fDir.getAbsolutePath()+File.separatorChar+path,subject);
		}
		return null;
	}
	
	private static Properties examineStep2(String projectHome,Logger log,String path){
		String dir = System.getProperty(projectHome);
		String subject = new StringBuilder().append("[").append("Variabile di sistema ").append(projectHome).append("] ").toString();
		if(dir!=null){
			File fDir = new File(dir);
			if(!fDir.exists()){
				String errorMsg = new StringBuilder().append(subject).append("Directory non esistente: ").append(fDir.getAbsolutePath()).toString();
				log.error(errorMsg);
				return null;
			}
			if(!fDir.canRead()){
				String errorMsg = new StringBuilder().append(subject).append("Directory non accessibile: ").append(fDir.getAbsolutePath()).toString();
				log.error(errorMsg);
				return null;
			}
			return PropertiesUtilities.getPropertiesReader(log,fDir.getAbsolutePath()+File.separatorChar+path,subject);
		}
		return null;
	}
	
	private static Properties examineStep3(Logger log,String path){
		return PropertiesUtilities.getPropertiesReader(log, PropertiesUtilities.class.getResourceAsStream("/"+path), 
				new StringBuilder().append("CLASSPATH: ").append(path).toString());
	}
	
	private static Properties examineStep4(Logger log,String path,File fConfDirectory){
		File f = new File(fConfDirectory,path);
		if(f.exists()){
			return PropertiesUtilities.getPropertiesReader(log,f.getAbsolutePath(), 
					new StringBuilder().append("CONFID_DIR/").append(path).toString());
		}
		return null;
	}
	
	private static Properties getPropertiesReader(Logger log,String path,String subject){
		if(path!=null){
			File f = new File(path);
			if(!f.exists()){
				return null;
			}
			if(!f.canRead()){
				String errorMsg = new StringBuilder().append(subject).append("File di properties non accessibile: ").append(f.getAbsolutePath()).toString();
				log.error(errorMsg);
				return null;
			}
			try (InputStream is = new FileInputStream(f)){
				return PropertiesUtilities.getPropertiesReader(log,is,subject);
			}catch(java.io.IOException e) {
				String errorMsg = new StringBuilder().append(subject).append("File di properties non utilizzabile: ").append(e.getMessage()).toString();
				log.error(errorMsg,e);
			}
		}
		return null;
	}
	private static Properties getPropertiesReader(Logger log,InputStream is,String subject){
		if(is!=null){
			Properties propertiesReader = null;
			try{  
				propertiesReader = new Properties();
				propertiesReader.load(is);
			}catch(java.io.IOException e) {
				propertiesReader = null;
				String errorMsg = new StringBuilder().append(subject).append("File di properties non utilizzabile: ").append(e.getMessage()).toString();
				log.error(errorMsg,e);
			}finally{
				try{
					is.close();
				}catch(Exception eClose){
					log.error(eClose.getMessage(),eClose);
				}
			}
				
			if(propertiesReader!=null){
				return propertiesReader;
			}
			return null;
		}
		return null;
	}
	
}
