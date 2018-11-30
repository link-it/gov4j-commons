package org.gov4j.commons.core.properties;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;

public class LocalPropertiesReader {

	private PropertiesReader propertiesOriginale;
	private CollectionProperties propertiesRidefinitoFile;
	protected Logger log;
	private String PROJECT_HOME;
	
	public LocalPropertiesReader(String PROJECT_HOME,String propertiesOriginale,Logger log) throws Exception{
		PropertiesReader pReader = new PropertiesReader(propertiesOriginale);
		this.init(PROJECT_HOME, pReader.getProperties(), log);
	}
	public LocalPropertiesReader(String PROJECT_HOME,Properties propertiesOriginale,Logger log) throws Exception{
		this.init(PROJECT_HOME, propertiesOriginale, log);
	}
	private void init(String PROJECT_HOME,Properties propertiesOriginale,Logger log) {
		this.propertiesOriginale = new PropertiesReader(propertiesOriginale);
		this.log = log;
		if(this.log==null){
			this.log = org.slf4j.LoggerFactory.getLogger(LocalPropertiesReader.class);
		}
		this.PROJECT_HOME = PROJECT_HOME;
	}
	
	public void setLocalFileImplementation(String localFileName){
		String confDirectory = null;
		try {
			confDirectory = this.propertiesOriginale.getProperty(Constants.PROPERTIES_CONF_DIR, false, true);
		}catch(PropertiesException pe) {}
		this.setLocalFileImplementation(localFileName, confDirectory);
	}
	public void setLocalFileImplementation(String localFileName,String confDirectory){
		CollectionProperties prop = PropertiesUtilities.searchLocalImplementation(this.PROJECT_HOME,this.log, localFileName, confDirectory);
		if(prop!=null)
			this.propertiesRidefinitoFile = prop;
	}
	
	public void setLocalFileImplementation(CollectionProperties prop){
		this.propertiesRidefinitoFile = prop;
	}
	
	
	
	// RIDEFINIZIONE METODI PROPERTIES READER
	
	public java.util.Enumeration<?> propertyNames(){
		
		java.util.Enumeration<?> enumProp = this.propertiesOriginale.propertyNames();
		Vector<String> object = new Vector<String>();
		while(enumProp.hasMoreElements()){
			object.add((String)enumProp.nextElement());		
		}
		
		if(this.propertiesRidefinitoFile!=null){
			java.util.Enumeration<?> enumPropRidefinito = this.propertiesRidefinitoFile.propertyNames();
			while(enumPropRidefinito!=null && enumPropRidefinito.hasMoreElements()){
				String ridefinito = (String)enumPropRidefinito.nextElement();
				if(object.contains(ridefinito)==false){
					object.add(ridefinito);		
				}
			}
		}
		
		return object.elements();
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
		String v = this.getValueEngine(key, convertEnv);
		if(v==null && required) {
			StringBuilder sb = new StringBuilder();
			sb.append("Proprietà [").append(key).append("] non trovata");
			throw new PropertiesException(sb.toString());
		}
		return v;
	}
	
	
	public java.util.Properties readProperties(String prefix, boolean required) throws PropertiesException {
		return this.readProperties(prefix, required, true, true);
	}
	public java.util.Properties readProperties(String prefix, boolean required, boolean convertEnvValue) throws PropertiesException {
		return this.readProperties(prefix, required, convertEnvValue, true);
	}
	public java.util.Properties readProperties(String prefix, boolean required, boolean convertEnvValue, boolean convertEnvKey) throws PropertiesException {
		java.util.Properties p = this.readPropertiesEngine(prefix, convertEnvValue, convertEnvKey);
		if(p==null || p.isEmpty()) {
			if(required) {
				StringBuilder sb = new StringBuilder();
				sb.append("Proprietà con prefisso [").append(prefix).append("] non trovate");
				throw new PropertiesException(sb.toString());
			}
		}
		return p;
	}

	
	
	
	/* ------------ UTILITY INTERNE ----------------- */
	private String getValueEngine(String key,boolean convertEnvProperties) throws PropertiesException{
		
		// NOTA: La logica di append "+," vale solo rispetto agli elementi PropertiesOriginale, CollectionProperties, PropertiesObject
		//		 Mentre il valore indicato per una proprietà dei files locali all'interno del CollectionProperties non va in append, ma viene utilizzato solamente il primo incontrato (che poi sarà utilizzato per come valore del 2. File).
				
		try{
					
			
			// 1. File
			String [] addFile = null;
			boolean appendFile = false;
			String tmpFile = null;
			if(this.propertiesRidefinitoFile!=null){
				tmpFile = this.propertiesRidefinitoFile.getProperty(key, convertEnvProperties);
			}
			if(tmpFile!=null){
				tmpFile = tmpFile.trim();
				String add = null;
				if(tmpFile.startsWith("+")){
					appendFile = true;
					add = tmpFile.substring(1);
					add = add.trim();
				}
				if(add!=null){
					if(add.startsWith(",") && add.length()>1){
						addFile = add.substring(1).split(",");
						if(addFile!=null && addFile.length>0){
							for (int i = 0; i < addFile.length; i++) {
								addFile[i] = addFile[i].trim();
							}
						}else{
							//return tmpFile; // valore strano, lo ritorno
							addFile = new String[1];
							addFile[0] = tmpFile;
						}
					}
				}else{
					//return tmpFile;
					addFile = new String[1];
					addFile[0] = tmpFile;
				}
			}
			
			
			// 2. Originale
			String tmp = null;
			if(tmp==null){
				tmp = this.propertiesOriginale.getProperty(key, false, convertEnvProperties);
			}
			
			
			// 3. Gestione valore ritornato
			if(addFile==null){
				return tmp;
			}
			if(!appendFile){
				// Tecnica append non utilizzato
				// Ritorno il primo valore disponibile rispetto all'ordine
				if(addFile!=null){
					return addFile[0];
				}
			}
			
			
			// 4. Se sono state fornite proprieta' con il + le gestisco.
			// In caso di valori uguali utilizzo quelli dell'object, poi quelli del file e infine quelli dell'originale
			StringBuffer bf = new StringBuffer();
			List<String> valoriAggiunti = new ArrayList<String>();
			if(addFile!=null){
				for (int i = 0; i < addFile.length; i++) {
					if(valoriAggiunti.contains(addFile[i])==false){
						if(bf.length()>0){
							bf.append(",");
						}
						bf.append(addFile[i]);
						valoriAggiunti.add(addFile[i]);
					}
				}
			}
			if(tmp!=null){
				if(valoriAggiunti.contains(tmp)==false){
					if(bf.length()>0){
						bf.append(",");
					}
					bf.append(tmp);
					valoriAggiunti.add(tmp);
				}
			}
			
			return bf.toString();
			
		}catch(Exception e){
			this.log.error(new StringBuilder().append("Errore durante la lettura della proprietà [").append(key).append("](").append(convertEnvProperties).append(")").toString(),e);
			throw new PropertiesException(e.getMessage(),e);
		}
		
	}
	
	private java.util.Properties readPropertiesEngine(String prefix,boolean convertEnvProperties, boolean convertEnvKey)throws PropertiesException{
		
		java.util.Properties tmp = null;
		try{
		
			tmp = this.propertiesOriginale.readProperties(prefix, false, convertEnvProperties, convertEnvKey);
			
			if(this.propertiesRidefinitoFile!=null){
				java.util.Properties tmp2 = this.propertiesRidefinitoFile.readProperties(prefix, convertEnvProperties);
				if(tmp2!=null){
					Enumeration<?> keys = tmp2.keys();
					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();
						if(tmp.containsKey(key)){
							tmp.remove(key);
						}
						tmp.put(key, tmp2.get(key));
					}
				}
			}
			
		}catch(Exception e){
			this.log.error(new StringBuilder().append("Errore durante la lettura della proprietà con prefix [").append(prefix).append("](").append(convertEnvProperties).append(")").toString(),e);
			throw new PropertiesException(e.getMessage(),e);
		}
		
		return tmp;
	}
}
