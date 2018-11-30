package org.gov4j.commons.xml.xsd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.gov4j.commons.core.exception.CoreException;
import org.gov4j.commons.xml.XMLUtils;
import org.slf4j.Logger;

public class ValidatoreXSDFactory {

	private static ValidatoreXSDFactory factory = null;
	private static synchronized void init(){
		if(ValidatoreXSDFactory.factory==null){
			ValidatoreXSDFactory.factory = new ValidatoreXSDFactory();
		}
	}
	public static ValidatoreXSDFactory getInstance(){
		if(ValidatoreXSDFactory.factory==null){
			ValidatoreXSDFactory.init();
		}
		return ValidatoreXSDFactory.factory;
	}	
	
	private static final String XSD_INIT_ERROR = "Init xsd schema failure: ";
	
	
	public ValidatoreXSD newInstance(Logger log,String xsdPath,String ... xsdImported) throws CoreException{

		/* --- XSD Validator -- */
		if(xsdPath == null){
			throw new CoreException("XsdPath is null");
		}

		// check schema
		InputStream isSchema = this.readPath(xsdPath);
		
		// check schema imported
		List<InputStream> listInputStreams = new ArrayList<>();
		XSDResourceResolver xsdResourceResolver = this.readXSDResourceResolver(isSchema, listInputStreams, xsdImported);
		
		// init schema Validator
		return this.init(log, isSchema, listInputStreams, xsdResourceResolver);
	}
	
	private InputStream readPath(String xsdPath) throws CoreException {
		InputStream isSchema = null;
		try{
			File f = new File(xsdPath);
			if(f.exists()){
				isSchema = new FileInputStream(f);
			}else{
				isSchema = ValidatoreXSDFactory.class.getResourceAsStream(xsdPath);
				if(isSchema==null){
					isSchema = ValidatoreXSDFactory.class.getResourceAsStream("/"+xsdPath);
				}
			}
			if(isSchema==null){
				StringBuilder sb = new StringBuilder();
				sb.append("Creating InputStream from xsdPath [").append(xsdPath).append("] failure");
				throw new CoreException(sb.toString());
			}
		}catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(XSD_INIT_ERROR).append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}
		return isSchema;
	}
	
	private XSDResourceResolver readXSDResourceResolver(InputStream isSchema, List<InputStream> listInputStreams, String ... xsdImported) throws CoreException {
		if(xsdImported!=null && xsdImported.length>0){
			XSDResourceResolver xsdResourceResolver = new XSDResourceResolver();
			
			for (int i = 0; i < xsdImported.length; i++) {
				InputStream is = null;
				try{
					File f = new File(xsdImported[i]);
					if(f.exists()){
						is = new FileInputStream(f);
					}else{
						is = ValidatoreXSDFactory.class.getResourceAsStream(xsdImported[i]);
						if(is==null){
							is = ValidatoreXSDFactory.class.getResourceAsStream("/"+xsdImported[i]);
						}
					}
					if(is==null){
						StringBuilder sb = new StringBuilder();
						sb.append("Creating InputStream from xsdPath[").append(i).append("][").append(xsdImported[i]).append("] failure");
						throw new CoreException(sb.toString());
					}
					xsdResourceResolver.addResource(f.getName(), is);
					listInputStreams.add(is);
				}catch (Exception e) {
					this.throwExceptionAndCloseResources(isSchema, listInputStreams, e);
				} finally {
					if(is!=null) try {is.close();} catch(Exception e) {}
				}
			}
			
			return xsdResourceResolver;
		}
		return null;
	}
	
	private ValidatoreXSD init(Logger log, InputStream isSchema,List<InputStream> listInputStreams, XSDResourceResolver xsdResourceResolver) throws CoreException {
		try{
			ValidatoreXSD validatore = null;
			if(xsdResourceResolver!=null){
				validatore = new ValidatoreXSD(log, XMLUtils.getInstance().getSchemaFactory().getClass().getName(), xsdResourceResolver, isSchema);
			}else{
				validatore = new ValidatoreXSD(log, XMLUtils.getInstance().getSchemaFactory().getClass().getName(), isSchema);
			}
			return validatore;
		}catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(XSD_INIT_ERROR).append(e.getMessage());
			throw new CoreException(sb.toString(),e);
		}finally{
			this.closeResources(isSchema, listInputStreams);
		}
	}

	private void throwExceptionAndCloseResources(InputStream isSchema, List<InputStream> listInputStreams, Exception e) throws CoreException {
		this.closeResources(isSchema, listInputStreams);
		StringBuilder sb = new StringBuilder();
		sb.append(XSD_INIT_ERROR).append(e.getMessage());
		throw new CoreException(sb.toString(),e);
	}
	private void closeResources(InputStream isSchema, List<InputStream> listInputStreams) throws CoreException {
		try{
			if(isSchema!=null){
				isSchema.close();
			}
		}catch(Exception eClose){}
		for (InputStream inputStream : listInputStreams) {
			try{
				if(inputStream!=null){
					inputStream.close();
				}
			}catch(Exception eClose){}
		}
	}
}
