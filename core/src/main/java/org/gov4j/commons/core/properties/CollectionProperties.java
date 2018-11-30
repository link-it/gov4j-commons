package org.gov4j.commons.core.properties;

import java.util.Vector;


public class CollectionProperties {

	/*
	 * // RICERCA
		// 1. VARIABILE DI SISTEMA: PROJECT_HOME dove deve essere specificata una directory in cui verra' cercato il file path
		// 2. PROPRIETA' JAVA (es. ApplicationServer o Java con -D): PROJECT_HOME dove deve essere specificata una directory in cui verra' cercato il file path
		// 3. CLASSPATH con nome path
		// 4. (DIRECTORY DI CONFIGURAZIONE)/path
	*/
	
	// NOTA: La logica di append "+," vale solo per le proprietà che sono definite dentro InstanceProperties e cioè viene usato per aggiungere nuovi valori
	//	     rispetto agli elementi PropertiesOriginale, CollectionProperties, PropertiesObject
	//		 Mentre il valore indicato per una proprietà dei files locale (Properties definiti in questa classi) viene identificato secondo l'algoritmo 1-4 
	//	     e non va in append, ma viene utilizzato solamente il primo incontrato.
	
	private PropertiesReader systemProjectHome;
	private PropertiesReader javaProjectHome;
	private PropertiesReader classpath;
	private PropertiesReader configDir;
	
	public PropertiesReader getSystemProjectHome() {
		return this.systemProjectHome;
	}
	public void setSystemProjectHome(PropertiesReader systemProjectHome) {
		this.systemProjectHome = systemProjectHome;
	}
	public PropertiesReader getJavaProjectHome() {
		return this.javaProjectHome;
	}
	public void setJavaProjectHome(PropertiesReader javaProjectHome) {
		this.javaProjectHome = javaProjectHome;
	}
	public PropertiesReader getClasspath() {
		return this.classpath;
	}
	public void setClasspath(PropertiesReader classpath) {
		this.classpath = classpath;
	}
	public PropertiesReader getConfigDir() {
		return this.configDir;
	}
	public void setConfigDir(PropertiesReader configDir) {
		this.configDir = configDir;
	}
	
	public java.util.Enumeration<?> propertyNames(){
		return this._keys().elements();
	}
	public java.util.Enumeration<?> keys(){ // WRAPPER per java.util.Properties.
		return this._keys().elements();
	}
	public int size(){ // WRAPPER per java.util.Properties.
		return this._keys().size();
	}
	
	private Vector<String> _keys(){
	
		Vector<String> keys = new Vector<String>();
		
		if(this.systemProjectHome!=null){
			java.util.Enumeration<?> enumProp = this.systemProjectHome.propertyNames();
			while(enumProp.hasMoreElements()){
				String key = (String)enumProp.nextElement();
				if(keys.contains(key)==false)
					keys.add(key);		
			}
		}
		
		if(this.javaProjectHome!=null){
			java.util.Enumeration<?> enumProp = this.javaProjectHome.propertyNames();
			while(enumProp.hasMoreElements()){
				String key = (String)enumProp.nextElement();
				if(keys.contains(key)==false)
					keys.add(key);		
			}
		}
		
		if(this.classpath!=null){
			java.util.Enumeration<?> enumProp = this.classpath.propertyNames();
			while(enumProp.hasMoreElements()){
				String key = (String)enumProp.nextElement();
				if(keys.contains(key)==false)
					keys.add(key);			
			}
		}
		
		if(this.configDir!=null){
			java.util.Enumeration<?> enumProp = this.configDir.propertyNames();
			while(enumProp.hasMoreElements()){
				String key = (String)enumProp.nextElement();
				if(keys.contains(key)==false)
					keys.add(key);		
			}
		}
		
		
		
		return keys;
	}
	
	public String getProperty(String key,boolean convertEnv)throws PropertiesException{
	
		if(this.systemProjectHome!=null){
			String v = this.systemProjectHome.getProperty(key, false, convertEnv);
			if(v!=null){
				return v;
			}
		}
		
		if(this.javaProjectHome!=null){
			String v = this.javaProjectHome.getProperty(key, false, convertEnv);
			if(v!=null){
				return v;
			}
		}
		
		if(this.classpath!=null){
			String v = this.classpath.getProperty(key, false, convertEnv);
			if(v!=null){
				return v;
			}
		}
		
		if(this.configDir!=null){
			String v = this.configDir.getProperty(key, false, convertEnv);
			if(v!=null){
				return v;
			}
		}
		
		return null;
		
	}
	
	public String getProperty(String key) { // WRAPPER per java.util.Properties. Non deve lanciare eccezione
		return this.get(key);
	}
	public String get(String key) { // WRAPPER per java.util.Properties. Non deve lanciare eccezione
		try{
			return this.getProperty(key, true);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("Lettura proprietà [").append(key).append("] ha generato un errore: ").append(e.getMessage());
			org.slf4j.LoggerFactory.getLogger(CollectionProperties.class).error(sb.toString(),e);
			return null;
		}
	}
	
	
	public java.util.Properties readProperties(String prefix, boolean convertEnvValue)throws PropertiesException{
		java.util.Properties prop = new java.util.Properties();
		try{
			
			java.util.Enumeration<?> keys = this.propertyNames(); // property names di questa classe colleziona tutti i nomi di tutti i files esterni
			if(keys!=null){
				while (keys.hasMoreElements()) {
					Object keyIt = (Object) keys.nextElement();
					if(keyIt instanceof String){
						String property = (String) keyIt;
						if(property.startsWith(prefix)){
							String key = (property.substring(prefix.length()));
							if(key != null)
								key = key.trim();
							String value = this.getProperty(property, true);
							if(value!=null)
								value = ((String)value).trim();
							if(key!=null && value!=null){
								prop.setProperty(key,(String) value);
							}
						}
					}
				}
			}
			
			return prop;
		}catch(java.lang.Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Riscontrato errore durante la lettura delle propriete con prefisso [").append(prefix).append("]: ").append(e.getMessage());
			throw new PropertiesException(sb.toString(),e);
		}  
	}
	
}
