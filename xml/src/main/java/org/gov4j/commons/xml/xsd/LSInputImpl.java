package org.gov4j.commons.xml.xsd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.w3c.dom.ls.LSInput;

public class LSInputImpl implements LSInput {

	private String type;  // http://www.w3.org/2001/XMLSchema
	private String namespaceURI;
	private String publicId;
	private String systemId; // es. esempio.xsd
	private String baseURI; // es. /etc/govway/esempio.xsd
	private byte[] resource;
	private String encoding = Charset.defaultCharset().name();

	public LSInputImpl(String type, String namespaceURI, String publicId, String systemId,
			String baseURI, byte[] resource){
		this.type = type;
		this.namespaceURI = namespaceURI;
		this.publicId = publicId;
		this.systemId = systemId;
		this.baseURI = baseURI;
		this.resource = resource;
	} 
	
	@Override
	public Reader getCharacterStream() {
		try{
			return new InputStreamReader(new ByteArrayInputStream(this.resource),this.encoding);
		}catch(Exception e){
			throw new RuntimeException("Metodo getCharacterStream() ha causato un errore",e);
		}
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
		try{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int letti = 0;
			char [] buffer = new char[1024];
			while( (letti=characterStream.read(buffer)) != -1 ){
				for(int i=0;i<letti;i++)
					bout.write(buffer[i]);
			}
			this.resource = bout.toByteArray();
		}catch(Exception e){
			throw new RuntimeException("Metodo setCharacterStream(Reader characterStream) ha causato un errore",e);
		}
	}

	@Override
	public InputStream getByteStream() {
		return new ByteArrayInputStream(this.resource);
	}

	@Override
	public void setByteStream(InputStream byteStream) {
		try{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int letti = 0;
			byte[] buffer = new byte[1024];
			while( (letti=byteStream.read(buffer)) != -1 ){
				for(int i=0;i<letti;i++)
					bout.write(buffer,0,letti);
			}
			this.resource = bout.toByteArray();
		}catch(Exception e){
			throw new RuntimeException("Metodo setByteStream(InputStream byteStream) ha causato un errore",e);
		}
	}

	@Override
	public String getStringData() {
		try{
			return new String(this.resource,this.encoding);
		}catch(Exception e){
			throw new RuntimeException("Metodo getStringData() ha causato un errore",e);
		}
	}

	@Override
	public void setStringData(String stringData) {
		this.resource = stringData.getBytes();
	}

	@Override
	public String getSystemId() {
		return this.systemId;
	}

	@Override
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String getPublicId() {
		return this.publicId;
	}

	@Override
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	@Override
	public String getBaseURI() {
		return this.baseURI;
	}

	@Override
	public void setBaseURI(String baseURI){ 
		this.baseURI = baseURI;
	}

	@Override
	public String getEncoding() {
		return this.encoding; 
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public boolean getCertifiedText() {
		throw new RuntimeException("Metodo getCertifiedText() non implementato");
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
		throw new RuntimeException("Metodo setCertifiedText(boolean certifiedText) non implementato");
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNamespaceURI() {
		return this.namespaceURI;
	}

	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}

	public byte[] getResource() {
		return this.resource;
	}

	public void setResource(byte[] resource) {
		this.resource = resource;
	}
}
