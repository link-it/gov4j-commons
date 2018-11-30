package org.gov4j.commons.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	
	private Properties properties;
	
	public PropertiesReader(String name) throws PropertiesException{
		try{  
			File f = new File(name);
			if(f.exists()){
				try(FileInputStream fin = new FileInputStream(f)){
					this.properties = new Properties();
					this.properties.load(fin);
				}
			}
			else{
				try(InputStream is = PropertiesReader.class.getResourceAsStream(name);){
					if(is!=null) {
						this.properties = new Properties();
						this.properties.load(is);
					}
				}
				if(this.properties==null && !name.startsWith("/")) {
					StringBuilder sb = new StringBuilder();
					sb.append("/").append(name);
					try(InputStream is = PropertiesReader.class.getResourceAsStream(sb.toString());){
						if(is!=null) {
							this.properties = new Properties();
							this.properties.load(is);
						}
					}
				}
				if(this.properties==null) {
					throw new Exception("Properties '"+name+"' not found");
				}
			}
		}catch(Exception e) {
			throw new PropertiesException(e.getMessage(),e);
		}	
	}
	public PropertiesReader(Properties properties){
		this.properties = properties;
	}
	
	
	public java.util.Enumeration<?> propertyNames(){
		return this.properties.keys();
	}
	public Properties getProperties() {
		return this.properties;
	}
	
	public Integer getIntegerProperty(String key, boolean required) throws PropertiesException {
		return this.getIntegerProperty(key, required, true);
	}
	public Integer getIntegerProperty(String key, boolean required, boolean convertEnv) throws PropertiesException {
		String tmp = this.getProperty(key, required, convertEnv);
		if(tmp!=null) {
			try {
				return Integer.valueOf(tmp);
			}catch(Exception e) {
				throw new PropertiesException(e.getMessage(),e);
			}
		}
		return null;
	}
	
	public Long getLongProperty(String key, boolean required) throws PropertiesException {
		return this.getLongProperty(key, required, true);
	}
	public Long getLongProperty(String key, boolean required, boolean convertEnv) throws PropertiesException {
		String tmp = this.getProperty(key, required, convertEnv);
		if(tmp!=null) {
			try {
				return Long.valueOf(tmp);
			}catch(Exception e) {
				throw new PropertiesException(e.getMessage(),e);
			}
		}
		return null;
	}
	
	public Boolean getBooleanProperty(String key, boolean required) throws PropertiesException {
		return this.getBooleanProperty(key, required, true);
	}
	public Boolean getBooleanProperty(String key, boolean required, boolean convertEnv) throws PropertiesException {
		String tmp = this.getProperty(key, required, convertEnv);
		if(tmp!=null) {
			try {
				return Boolean.valueOf(tmp);
			}catch(Exception e) {
				throw new PropertiesException(e.getMessage(),e);
			}
		}
		return null;
	}
	
	public String getProperty(String key, boolean required) throws PropertiesException {
		return this.getProperty(key, required, true);
	}
	public String getProperty(String key, boolean required, boolean convertEnv) throws PropertiesException {
		String value = this.properties.getProperty(key);
		if(value!=null) {
			value = value.trim();
			if(convertEnv) {
				return this.convertEnvProperties(value);
			}
			else {
				return value;
			}
		}
		else{
			if(required) {
				StringBuilder sb = new StringBuilder();
				sb.append("Proprietà [").append(key).append("] non trovata");
				throw new PropertiesException(sb.toString());
			}
			return null;
		}
	}
	
	public java.util.Properties readProperties(String prefix, boolean required) throws PropertiesException {
		return this.readProperties(prefix, required, true, true);
	}
	public java.util.Properties readProperties(String prefix, boolean required, boolean convertEnvValue) throws PropertiesException {
		return this.readProperties(prefix, required, convertEnvValue, true);
	}
	public java.util.Properties readProperties(String prefix, boolean required, boolean convertEnvValue, boolean convertEnvKey) throws PropertiesException {
		java.util.Properties p = null;
		if(convertEnvValue) {
			p = this.readProperties_convertEnvProperties(prefix, convertEnvKey);
		}
		else {
			p = this.readProperties(prefix);
		}
		if(p==null || p.isEmpty()) {
			if(required) {
				StringBuilder sb = new StringBuilder();
				sb.append("Proprietà con prefisso [").append(prefix).append("] non trovate");
				throw new PropertiesException(sb.toString());
			}
		}
		return p;
	}
	
	
	// PRIVATE UTILS
	
	private java.util.Properties readProperties (String prefix)throws PropertiesException{
		java.util.Properties prop = new java.util.Properties();
		try{
			
			java.util.Enumeration<?> keys = this.properties.keys();
			while (keys.hasMoreElements()) {
				Object keyIt = keys.nextElement();
				if(keyIt instanceof String){
					String property = (String) keyIt;
					if(property.startsWith(prefix)){
						String key = (property.substring(prefix.length()));
						if(key != null)
							key = key.trim();
						String value = this.properties.getProperty(property);
						if(value!=null)
							value = value.trim();
						if(key!=null && value!=null){
							prop.setProperty(key,value);
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
	
	private java.util.Properties readProperties_convertEnvProperties (String prefix, boolean convertKeyEnvProperties)throws PropertiesException{
		java.util.Properties prop = new java.util.Properties();
		try{ 
			java.util.Properties prop_tmp = this.readProperties(prefix);
			java.util.Enumeration<?> en = prop_tmp.propertyNames();
			for (; en.hasMoreElements() ;) {
				String property = (String) en.nextElement();
				String value = prop_tmp.getProperty(property);
				if(value!=null){
					value = value.trim();
					value = this.convertEnvProperties(value);
				}
				if(property!=null && value!=null){
					if(convertKeyEnvProperties){
						prop.setProperty(this.convertEnvProperties(property,true),value);
					}
					else{
						prop.setProperty(property,value);
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
	
	private String convertEnvProperties(String value)throws PropertiesException{
		return this.convertEnvProperties(value, false);
	}
	private String convertEnvProperties(String value, boolean convertKeyEnvProperties)throws PropertiesException{
		String label = "del valore";
		if(convertKeyEnvProperties){
			label = "della chiave";
		}
		if(value.indexOf("${")!=-1){
			while (value.indexOf("${")!=-1){
				int indexStart = value.indexOf("${");
				int indexEnd = value.indexOf("}");
				if(indexEnd==-1){
					StringBuilder sb = new StringBuilder();
					sb.append("Errore durante l'interpretazione ").append(label).append(" [").append(value).append("]: ${ utilizzato senza la rispettiva chiusura }");
					throw new PropertiesException(sb.toString());
				}
				String nameSystemProperty = value.substring(indexStart+"${".length(),indexEnd);
				String valueSystemProperty = System.getProperty(nameSystemProperty);
				if(valueSystemProperty==null){
					StringBuilder sb = new StringBuilder();
					sb.append("Errore durante l'interpretazione ").append(label).append(" [").append(value).append("]: variabile di sistema ${").append(nameSystemProperty).append("} non esistente");
					throw new PropertiesException(sb.toString());
				}
				StringBuilder sb = new StringBuilder();
				sb.append("${").append(nameSystemProperty).append("}");
				value = value.replace(sb.toString(), valueSystemProperty);
			}
		}
		return value;
	}
	
}
