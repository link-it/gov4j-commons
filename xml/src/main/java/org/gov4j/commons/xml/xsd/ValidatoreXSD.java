package org.gov4j.commons.xml.xsd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.gov4j.commons.core.exception.CoreException;
import org.gov4j.commons.core.exception.ValidationException;
import org.gov4j.commons.xml.XMLUtils;
import org.slf4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;


public class ValidatoreXSD {


	/** StreamSource */
	private Schema schema;
	public Schema getSchema() {
		return this.schema;
	}
	
	
	/** ----------------- COSTRUTTORE INPUT STREAM ----------------- */
	
	private static final String FACTORY_DEFAULT = "FactoryDefault";
	
	/** 
	 * Costruttore InputStream
	 * 
	 * @param inputStream
	 */
	public ValidatoreXSD(Logger log,InputStream inputStream)throws CoreException{
		this(log,ValidatoreXSD.FACTORY_DEFAULT,inputStream);
	}
	public ValidatoreXSD(Logger log,String schemaFactory,InputStream inputStream)throws CoreException{
		try{
			StreamSource streamSource = new StreamSource(inputStream);	
			this.initializeSchema(log,schemaFactory,null,streamSource);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("Riscontrato errore durante la costruzione dello schema (InputStream): ").append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}
	}
	
	public ValidatoreXSD(Logger log,LSResourceResolver lsResourceResolver,InputStream inputStream)throws CoreException{
		this(log,ValidatoreXSD.FACTORY_DEFAULT,lsResourceResolver,inputStream);
	}
	public ValidatoreXSD(Logger log,String schemaFactory,LSResourceResolver lsResourceResolver,InputStream inputStream)throws CoreException{
		try{
			StreamSource streamSource = new StreamSource(inputStream);	
			this.initializeSchema(log,schemaFactory,lsResourceResolver,streamSource);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("Riscontrato errore durante la costruzione dello schema (LSResourceResolver e InputStream): ").append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}
	}
	
	public ValidatoreXSD(Logger log,InputStream... inputStream)throws CoreException{
		this(log,ValidatoreXSD.FACTORY_DEFAULT,inputStream);
	}
	public ValidatoreXSD(Logger log,String schemaFactory,InputStream... inputStream)throws CoreException{
		try{
			StreamSource [] ss = new StreamSource[inputStream.length];
			for(int i=0; i<inputStream.length; i++){
				ss[i] = new StreamSource(inputStream[i]);
			}
			this.initializeSchema(log,schemaFactory,null,ss);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("Riscontrato errore durante la costruzione dello schema (InputStream[]): ").append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}
	}
	
	public ValidatoreXSD(Logger log,LSResourceResolver lsResourceResolver,InputStream... inputStream)throws CoreException{
		this(log,ValidatoreXSD.FACTORY_DEFAULT,lsResourceResolver,inputStream);
	}
	public ValidatoreXSD(Logger log,String schemaFactory,LSResourceResolver lsResourceResolver,InputStream... inputStream)throws CoreException{
		try{
			StreamSource [] ss = new StreamSource[inputStream.length];
			for(int i=0; i<inputStream.length; i++){
				ss[i] = new StreamSource(inputStream[i]);
			}
			this.initializeSchema(log,schemaFactory,lsResourceResolver,ss);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("Riscontrato errore durante la costruzione dello schema (LSResourceResolver e InputStream[]): ").append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}
	}
	
	
	

	

	
	
	
	/** ----------------- INIT SCHEMA ----------------- **/
		
	/**
	 * Metodo che si occupa di inizializzare lo schema per la validazione.
	 *
	 * 
	 */
	private void initializeSchema(Logger log,String schemaFactory,LSResourceResolver lsResourceResolver,Source streamSource) throws CoreException{
		try{
			
			// La gestione dello schemaFactory e' servito per il seguente motivo:
			// UndeclaredPrefix: Cannot resolve 'messaggioSII:xxxxMessaggioSIIType' as a QName: the prefix 'messaggioSII' is not declared.
			// After some debugging, I've found out that this is a bug of the JAXP api's built in to the JDK.
			// You can fix it by making sure that you use the Xerces version of the SchemaFactory, and not the JDK internal one. 
			// The algorithm for choosing a SchemaFactory is explained at http://java.sun.com/j2se/1.5.0/docs/api/javax/xml/validation/SchemaFactory.html#newInstance(java.lang.String).
			// It comes down to setting the System property "javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema" to the value "org.apache.xerces.jaxp.validation.XMLSchemaFactory".
			// Note that just adding Xerces to your classpath won't fix this, for reasons explained at http://xerces.apache.org/xerces2-j/faq-general.html#faq-4
			
			SchemaFactory factory = null;
			if(schemaFactory!=null && !ValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory)){
				factory =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI,
						schemaFactory, this.getClass().getClassLoader());
			}
			else{
				factory = XMLUtils.getInstance().getSchemaFactory();
			}
			
//			String oldSchemaFactorySetup = null;
//			String propertySystem = "javax.xml.validation.SchemaFactory:"+XMLConstants.W3C_XML_SCHEMA_NS_URI;
//			if(schemaFactory!=null && !AbstractValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory)){
//				oldSchemaFactorySetup = System.getenv(propertySystem);
//				if(oldSchemaFactorySetup==null){
//					oldSchemaFactorySetup = System.getProperty(propertySystem);
//					if(oldSchemaFactorySetup==null){
//						oldSchemaFactorySetup = "";
//					}
//				}
//				System.setProperty(propertySystem, schemaFactory);
//			}
//			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			log.info("SchemaFactory["+factory.getClass().getName()+"]");
			if(lsResourceResolver!=null){
				factory.setResourceResolver(lsResourceResolver);
			}
			this.schema = factory.newSchema(streamSource);
//			if(schemaFactory!=null && !AbstractValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory) && oldSchemaFactorySetup!=null){
//				log.debug("Ripristino oldSchemaFactory ["+oldSchemaFactorySetup+"]");
//				System.setProperty(propertySystem, oldSchemaFactorySetup);
//			}
		}catch (Exception e) {
			throw new CoreException("Riscontrato errore durante l'inizializzazione dello schema: "+e.getMessage(),e);
		}
	}
	private void initializeSchema(Logger log,String schemaFactory,LSResourceResolver lsResourceResolver,Source[] streamSource) throws CoreException{
		try{
			
			// La gestione dello schemaFactory e' servito per il seguente motivo:
			// UndeclaredPrefix: Cannot resolve 'messaggioSII:xxxxMessaggioSIIType' as a QName: the prefix 'messaggioSII' is not declared.
			// After some debugging, I've found out that this is a bug of the JAXP api's built in to the JDK.
			// You can fix it by making sure that you use the Xerces version of the SchemaFactory, and not the JDK internal one. 
			// The algorithm for choosing a SchemaFactory is explained at http://java.sun.com/j2se/1.5.0/docs/api/javax/xml/validation/SchemaFactory.html#newInstance(java.lang.String).
			// It comes down to setting the System property "javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema" to the value "org.apache.xerces.jaxp.validation.XMLSchemaFactory".
			// Note that just adding Xerces to your classpath won't fix this, for reasons explained at http://xerces.apache.org/xerces2-j/faq-general.html#faq-4
			
			SchemaFactory factory = null;
			if(schemaFactory!=null && !ValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory)){
				factory =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI,
						schemaFactory, this.getClass().getClassLoader());
			}
			else{
				factory = XMLUtils.getInstance().getSchemaFactory();
			}
			
//			String oldSchemaFactorySetup = null;
//			String propertySystem = "javax.xml.validation.SchemaFactory:"+XMLConstants.W3C_XML_SCHEMA_NS_URI;
//			if(schemaFactory!=null && !AbstractValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory)){
//				oldSchemaFactorySetup = System.getenv(propertySystem);
//				if(oldSchemaFactorySetup==null){
//					oldSchemaFactorySetup = System.getProperty(propertySystem);
//					if(oldSchemaFactorySetup==null){
//						oldSchemaFactorySetup = "";
//					}
//				}
//				System.setProperty(propertySystem, schemaFactory);
//			}
//			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			log.info("SchemaFactory["+factory.getClass().getName()+"]");
			if(lsResourceResolver!=null){
				factory.setResourceResolver(lsResourceResolver);
			}
			this.schema = factory.newSchema(streamSource);
//			if(schemaFactory!=null && !AbstractValidatoreXSD.FACTORY_DEFAULT.equals(schemaFactory) && oldSchemaFactorySetup!=null){
//				log.debug("Ripristino oldSchemaFactory ["+oldSchemaFactorySetup+"]");
//				System.setProperty(propertySystem, oldSchemaFactorySetup);
//			}
		}catch (Exception e) {
			throw new CoreException("Riscontrato errore durante l'inizializzazione dello schema: "+e.getMessage(),e);
		}
	}
	
	
	
	
	
	/** ----------------- VALIDAZIONI ----------------- */
	
	public void valida(Node nodeXML) throws ValidationException{
		valida(new DOMSource(nodeXML));
	}
	public void valida(Node nodeXML,ErrorHandler errorHandler) throws ValidationException{
		valida(new DOMSource(nodeXML),errorHandler);
	}
	
	public void valida(Node nodeXML,boolean streamSource) throws ValidationException, CoreException{
		this.valida(nodeXML,streamSource,null);
	}
	public void valida(Node nodeXML,boolean streamSource,ErrorHandler errorHandler) throws ValidationException, CoreException{
		if(streamSource){
			// Risolve il problema di validare gli attributi
			ByteArrayInputStream bin = new ByteArrayInputStream(XMLUtils.getInstance().toByteArray(nodeXML));
			valida(new StreamSource(bin),errorHandler);
		}else{
			valida(nodeXML,errorHandler);
		}
	}
	
	public void valida(InputStream inputStreamXML) throws ValidationException{
		valida(new StreamSource(inputStreamXML));
	}
	public void valida(InputStream inputStreamXML,ErrorHandler errorHandler) throws ValidationException{
		valida(new StreamSource(inputStreamXML),errorHandler);
	}
	
	public void valida(File fileXML) throws ValidationException{
		valida(new StreamSource(fileXML));
	}
	public void valida(File fileXML,ErrorHandler errorHandler) throws ValidationException{
		valida(new StreamSource(fileXML),errorHandler);
	}
	
	public void valida(String urlXML) throws ValidationException{
		valida(new StreamSource(urlXML));
	}
	public void valida(String urlXML,ErrorHandler errorHandler) throws ValidationException{
		valida(new StreamSource(urlXML),errorHandler);
	}
	
	public void valida(Source source) throws ValidationException{
		this.valida(source, null);
	}
	public void valida(Source source,ErrorHandler errorHandler) throws ValidationException{
		Validator validator  = this.schema.newValidator();
		try {
			if(errorHandler!=null){
				validator.setErrorHandler(errorHandler);
			}
			validator.validate(source);
		} catch (Exception e) {
			// instance document is invalid!
			throw new ValidationException(e.getMessage(),e);
		}
	}
	
	
}
