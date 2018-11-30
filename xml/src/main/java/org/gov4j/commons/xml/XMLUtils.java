package org.gov4j.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactory;

import org.gov4j.commons.core.exception.CoreException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;


public class XMLUtils {

	private static XMLUtils xmlUtils = null;
	private static synchronized void init(){
		if(XMLUtils.xmlUtils==null){
			XMLUtils.xmlUtils = new XMLUtils();
		}
	}
	public static XMLUtils getInstance(){
		if(XMLUtils.xmlUtils==null){
			XMLUtils.init();
		}
		return XMLUtils.xmlUtils;
	}	
	
    private static final String ENABLED = "yes";
	
	
	// XERCES
	private DocumentBuilderFactory documentFactory = null;
	private DatatypeFactory datatypeFactory = null;
//	private javax.xml.parsers.SAXParserFactory saxParserFactory = null;
//	private javax.xml.stream.XMLEventFactory xmlEventFactory = null;
	private SchemaFactory schemaFactory = null;
	
	// XALAN	
	private TransformerFactory transformerFactory = null;
	private XPathFactory xpathFactory = null;
	
	// OTHER
	private GregorianCalendar gregorianCalendar = null;
	
	
	
	
	
	
	
	// XERCES
	protected DocumentBuilderFactory newDocumentBuilderFactory() throws CoreException{
		try{
			// force xerces impl
			return DocumentBuilderFactory.newInstance(org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.class.getName(),this.getClass().getClassLoader());
		}catch(Exception e){
			throw new CoreException(e.getMessage(),e);
		}
	}
	protected DatatypeFactory newDatatypeFactory() throws CoreException{
		try{
			// force xerces impl
			return DatatypeFactory.newInstance(org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl.class.getName(), this.getClass().getClassLoader());
		}catch(Exception e){
			throw new CoreException(e.getMessage(),e);
		}
	}
	protected SchemaFactory newSchemaFactory() throws CoreException{
		try{
			// force xerces impl
			return SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI,
					org.apache.xerces.jaxp.validation.XMLSchemaFactory.class.getName(), this.getClass().getClassLoader());
		}catch(Exception e){
			throw new CoreException(e.getMessage(),e);
		}
	}
	
	// XALAN
	protected TransformerFactory newTransformerFactory() throws CoreException{
		try{
			// force xalan impl
			return TransformerFactory.newInstance(org.apache.xalan.processor.TransformerFactoryImpl.class.getName(), this.getClass().getClassLoader());
		}catch(Exception e){
			throw new CoreException(e.getMessage(),e);
		}
	}
	protected XPathFactory newXPathFactory() throws CoreException{
		try{
			// force xalan impl
			return XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI,
					org.apache.xpath.jaxp.XPathFactoryImpl.class.getName(), this.getClass().getClassLoader());
		}catch(Exception e){
			throw new CoreException(e.getMessage(),e);
		}
	}
	
	
	
	
	
	
	
	// INIT - XERCES
	
	public synchronized void initDocumentBuilderFactory() throws CoreException {
		if(this.documentFactory==null){
			try {
				this.documentFactory = newDocumentBuilderFactory();
			} catch (Exception e) {
				throw new CoreException(e.getMessage(),e);
			}
			this.documentFactory.setNamespaceAware(true);
		}
	}
	
	public synchronized void initDatatypeFactory() throws CoreException {
		if(this.datatypeFactory==null){
			try {
				this.datatypeFactory = newDatatypeFactory();
			} catch (Exception e) {
				throw new CoreException(e.getMessage(),e);
			}
		}
	}
	
	public synchronized void initSchemaFactory() throws CoreException {
		if(this.schemaFactory==null){
			try {
				this.schemaFactory = newSchemaFactory();
			} catch (Exception e) {
				throw new CoreException(e.getMessage(),e);
			}
		}
	}

	// INIT - XALAN
	
	public synchronized void initTransformerFactory() throws CoreException {
		if(this.transformerFactory==null){
			this.transformerFactory = newTransformerFactory();
		}
	}
	
	public synchronized void initXPathFactory() throws CoreException {
		if(this.xpathFactory==null){
			this.xpathFactory = newXPathFactory();
		}
	}
	
	// INIT - OTHER
	
	public synchronized void initCalendarConverter() throws CoreException {
		try{
			this.initDatatypeFactory();
			this.gregorianCalendar = (GregorianCalendar) Calendar.getInstance(); 
		} catch (Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
		
	}
	
	
	// GET - XERCES
	
	public DocumentBuilderFactory getDocumentBuilderFactory() throws CoreException {
		if(this.documentFactory==null){
			this.initDocumentBuilderFactory();
		}
		return this.documentFactory;
	}
	
	public DatatypeFactory getDatatypeFactory() throws CoreException {
		if(this.datatypeFactory==null){
			this.initDatatypeFactory();
		}
		return this.datatypeFactory;
	}
	
	public SchemaFactory getSchemaFactory() throws CoreException {
		if(this.schemaFactory==null){
			this.initSchemaFactory();
		}
		return this.schemaFactory;
	}
	
	
	// GET - XALAN

	public TransformerFactory getTransformerFactory() throws CoreException {
		if(this.transformerFactory==null){
			this.initTransformerFactory();
		}
		return this.transformerFactory;
	}
	
	public XPathFactory getXPathFactory() throws CoreException {
		if(this.xpathFactory==null){
			this.initXPathFactory();
		}
		return this.xpathFactory;
	}

	// GET - OTHER
	
	public XMLGregorianCalendar toGregorianCalendar(Date d) throws CoreException {
		if(this.datatypeFactory==null || this.gregorianCalendar==null){
			this.initCalendarConverter();
		}
		this.gregorianCalendar.setTime(d);
		return this.datatypeFactory.newXMLGregorianCalendar(this.gregorianCalendar);
	}



	// GET AS
	
	public Document getAsDocument(Element element) throws CoreException{
		return this.newDocument(this.toByteArray(element));
	}
	public Document getAsDocument(Element element,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(this.toByteArray(element),errorHandler);
	}
	public Document getAsDocument(Element element,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(this.toByteArray(element),entityResolver);
	}
	public Document getAsDocument(Element element,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(this.toByteArray(element),errorHandler,entityResolver);
	}
	
	public Document getAsDocument(Node Node) throws CoreException{
		return this.newDocument(this.toByteArray(Node));
	}
	public Document getAsDocument(Node Node,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(this.toByteArray(Node),errorHandler);
	}
	public Document getAsDocument(Node Node,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(this.toByteArray(Node),entityResolver);
	}
	public Document getAsDocument(Node Node,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(this.toByteArray(Node),errorHandler,entityResolver);
	}
	
	
	

	// NEW DOCUMENT

	public Document newDocument() throws CoreException{
		return this.newDocument_engine(new XMLErrorHandler(),null);
	}
	public Document newDocument(ErrorHandler errorHandler) throws CoreException{
		return this.newDocument_engine(errorHandler,null);
	}
	public Document newDocument(EntityResolver entityResolver) throws CoreException{
		return this.newDocument_engine(new XMLErrorHandler(),entityResolver);
	}
	public Document newDocument(ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument_engine(errorHandler,entityResolver);
	}
	private Document newDocument_engine(ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		try {
			DocumentBuilder documentBuilder = this.getDocumentBuilderFactory().newDocumentBuilder();
			documentBuilder.setErrorHandler(errorHandler);
			if(entityResolver!=null){
				documentBuilder.setEntityResolver(entityResolver);
			}
			return documentBuilder.newDocument();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	
	public Document newDocument(byte[] xml) throws CoreException{
		return this.newDocument(xml,new XMLErrorHandler(),null);
	}
	public Document newDocument(byte[] xml,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(xml,errorHandler,null);
	}
	public Document newDocument(byte[] xml,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(xml,new XMLErrorHandler(),entityResolver);
	}
	public Document newDocument(byte[] xml,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		ByteArrayInputStream bin = null;
		try{
			bin = new ByteArrayInputStream(xml);
			return this.newDocument(bin,errorHandler,entityResolver);
		}finally{
			if(bin!=null){
				try{
					bin.close();
				}catch(Exception eClose){}
			}
		}
	}

	public Document newDocument(InputStream is) throws CoreException{
		return this.newDocument(is,new XMLErrorHandler(),null);
	}
	public Document newDocument(InputStream is,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(is,errorHandler,null);
	}
	public Document newDocument(InputStream is,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,new XMLErrorHandler(),entityResolver);
	}
	public Document newDocument(InputStream is,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		try {
			DocumentBuilder documentBuilder = this.getDocumentBuilderFactory().newDocumentBuilder();
			documentBuilder.setErrorHandler(errorHandler);
			if(entityResolver!=null)
				documentBuilder.setEntityResolver(entityResolver);
			return documentBuilder.parse(is);
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}

	public Document newDocument(File f) throws CoreException{
		return this.newDocument(f,new XMLErrorHandler(),null);
	}
	public Document newDocument(File f,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(f,errorHandler,null);
	}
	public Document newDocument(File f,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(f,new XMLErrorHandler(),entityResolver);
	}
	public Document newDocument(File f,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		try {
			DocumentBuilder documentBuilder = this.getDocumentBuilderFactory().newDocumentBuilder();
			documentBuilder.setErrorHandler(errorHandler);
			if(entityResolver!=null){
				documentBuilder.setEntityResolver(entityResolver);
			}
			return documentBuilder.parse(f);
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}

	public Document newDocument(InputSource is) throws CoreException{
		return this.newDocument(is,new XMLErrorHandler(),null);
	}
	public Document newDocument(InputSource is,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(is,errorHandler,null);
	}
	public Document newDocument(InputSource is,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,new XMLErrorHandler(),entityResolver);
	}
	public Document newDocument(InputSource is,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		try {
			DocumentBuilder documentBuilder = this.getDocumentBuilderFactory().newDocumentBuilder();
			documentBuilder.setErrorHandler(errorHandler);
			if(entityResolver!=null){
				documentBuilder.setEntityResolver(entityResolver);
			}
			return documentBuilder.parse(is);
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}



	// NEW ELEMENT

	public Element newElement() throws CoreException{
		return this.newElement_engine(new XMLErrorHandler(),null);
	}
	public Element newElement(ErrorHandler errorHandler) throws CoreException{
		return this.newElement_engine(errorHandler,null);
	}
	public Element newElement(EntityResolver entityResolver) throws CoreException{
		return this.newElement_engine(new XMLErrorHandler(),entityResolver);
	}
	public Element newElement(ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newElement_engine(errorHandler,entityResolver);
	}
	private Element newElement_engine(ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		try {
			DocumentBuilder documentBuilder = this.getDocumentBuilderFactory().newDocumentBuilder();
			documentBuilder.setErrorHandler(errorHandler);
			if(entityResolver!=null){
				documentBuilder.setEntityResolver(entityResolver);
			}
			return documentBuilder.newDocument().getDocumentElement();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	
	public Element newElement(byte[] xml) throws CoreException{
		return this.newDocument(xml).getDocumentElement();
	}
	public Element newElement(byte[] xml,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(xml,errorHandler).getDocumentElement();
	}
	public Element newElement(byte[] xml,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(xml,entityResolver).getDocumentElement();
	}
	public Element newElement(byte[] xml,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(xml,errorHandler,entityResolver).getDocumentElement();
	}

	public Element newElement(InputStream is) throws CoreException{
		return this.newDocument(is).getDocumentElement();
	}
	public Element newElement(InputStream is,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(is,errorHandler).getDocumentElement();
	}
	public Element newElement(InputStream is,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,entityResolver).getDocumentElement();
	}
	public Element newElement(InputStream is,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,errorHandler,entityResolver).getDocumentElement();
	}

	public Element newElement(File f) throws CoreException{
		return this.newDocument(f).getDocumentElement();
	}
	public Element newElement(File f,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(f,errorHandler).getDocumentElement();
	}
	public Element newElement(File f,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(f,entityResolver).getDocumentElement();
	}
	public Element newElement(File f,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(f,errorHandler,entityResolver).getDocumentElement();
	}

	public Element newElement(InputSource is) throws CoreException{
		return this.newDocument(is).getDocumentElement();
	}
	public Element newElement(InputSource is,ErrorHandler errorHandler) throws CoreException{
		return this.newDocument(is,errorHandler).getDocumentElement();
	}
	public Element newElement(InputSource is,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,entityResolver).getDocumentElement();
	}
	public Element newElement(InputSource is,ErrorHandler errorHandler,EntityResolver entityResolver) throws CoreException{
		return this.newDocument(is,errorHandler,entityResolver).getDocumentElement();
	}



	// TO BYTE ARRAY

	public byte[] toByteArray(Document doc) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(doc,bout);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public byte[] toByteArray(Document doc,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(doc,bout,omitXMLDeclaration);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public byte[] toByteArray(Element element) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(element,bout);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public byte[] toByteArray(Element element,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(element,bout,omitXMLDeclaration);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public byte[] toByteArray(Node node) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(node,bout);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public byte[] toByteArray(Node node,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(node,bout,omitXMLDeclaration);
			bout.close();
			return bout.toByteArray();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}


	// TO STRING

	public String toString(Document doc) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(doc,bout);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public String toString(Document doc,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(doc,bout,omitXMLDeclaration);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public String toString(Element element) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(element,bout);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public String toString(Element element,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(element,bout,omitXMLDeclaration);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public String toString(Node node) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(node,bout);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	public String toString(Node node,boolean omitXMLDeclaration) throws CoreException{
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			this.writeTo(node,bout,omitXMLDeclaration);
			bout.close();
			return bout.toString();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}


	// WRITE TO

	public void writeTo(Document doc,OutputStream os)throws CoreException{
		this.writeNodeTo(doc, os, new XMLErrorListener(),false);
	}
	public void writeTo(Document doc,OutputStream os,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(doc, os,errorListener,false);
	}
	public void writeTo(Document doc,OutputStream os,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, os, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Document doc,OutputStream os,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, os,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Document doc,Writer writer)throws CoreException{
		this.writeNodeTo(doc, writer, new XMLErrorListener(),false);
	}
	public void writeTo(Document doc,Writer writer,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(doc, writer,errorListener,false);
	}
	public void writeTo(Document doc,Writer writer,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, writer, new XMLErrorListener(), omitXMLDeclaration);
	}
	public void writeTo(Document doc,Writer writer,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, writer,errorListener, omitXMLDeclaration);
	}

	public void writeTo(Document doc,File file)throws CoreException{
		this.writeNodeTo(doc, file, new XMLErrorListener(),false);
	}
	public void writeTo(Document doc,File file,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(doc, file,errorListener,false);
	}
	public void writeTo(Document doc,File file,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, file, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Document doc,File file,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(doc, file,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Element element,OutputStream os)throws CoreException{
		this.writeNodeTo(element, os, new XMLErrorListener(),false);
	}
	public void writeTo(Element element,OutputStream os,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(element, os,errorListener,false);
	}
	public void writeTo(Element element,OutputStream os,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, os, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Element element,OutputStream os,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, os,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Element element,Writer writer)throws CoreException{
		this.writeNodeTo(element, writer, new XMLErrorListener(),false);
	}
	public void writeTo(Element element,Writer writer,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(element, writer,errorListener,false);
	}
	public void writeTo(Element element,Writer writer,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, writer, new XMLErrorListener(), omitXMLDeclaration);
	}
	public void writeTo(Element element,Writer writer,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, writer,errorListener, omitXMLDeclaration);
	}

	public void writeTo(Element element,File file)throws CoreException{
		this.writeNodeTo(element, file, new XMLErrorListener(),false);
	}
	public void writeTo(Element element,File file,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(element, file,errorListener,false);
	}
	public void writeTo(Element element,File file,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, file, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Element element,File file,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(element, file,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Node node,OutputStream os)throws CoreException{
		this.writeNodeTo(node,os, new XMLErrorListener(),false);
	}
	public void writeTo(Node node,OutputStream os,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(node,os,errorListener,false);
	}
	public void writeTo(Node node,OutputStream os,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,os, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Node node,OutputStream os,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,os,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Node node,Writer writer)throws CoreException{
		this.writeNodeTo(node,writer, new XMLErrorListener(),false);
	}
	public void writeTo(Node node,Writer writer,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(node,writer,errorListener,false);
	}
	public void writeTo(Node node,Writer writer,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,writer, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Node node,Writer writer,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,writer,errorListener,omitXMLDeclaration);
	}

	public void writeTo(Node node,File file)throws CoreException{
		this.writeNodeTo(node,file, new XMLErrorListener(),false);
	}
	public void writeTo(Node node,File file,ErrorListener errorListener)throws CoreException{
		this.writeNodeTo(node,file,errorListener,false);
	}
	public void writeTo(Node node,File file,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,file, new XMLErrorListener(),omitXMLDeclaration);
	}
	public void writeTo(Node node,File file,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		this.writeNodeTo(node,file,errorListener,omitXMLDeclaration);
	}

	
	private void writeNodeTo(Node node,OutputStream os,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		try {
			Source source = new DOMSource(node);
			StreamResult result = new StreamResult(os);
			Transformer transformer = getTransformerFactory().newTransformer();
			if(omitXMLDeclaration)
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,ENABLED);
			transformer.setErrorListener(errorListener);
			transformer.transform(source, result);
			os.flush();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	private void writeNodeTo(Node node,Writer writer,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		try {
			Source source = new DOMSource(node);
			StreamResult result = new StreamResult(writer);
			Transformer transformer = getTransformerFactory().newTransformer();
			if(omitXMLDeclaration)
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,ENABLED);
			transformer.setErrorListener(errorListener);
			transformer.transform(source, result);
			writer.flush();
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}
	private void writeNodeTo(Node node,File file,ErrorListener errorListener,boolean omitXMLDeclaration)throws CoreException{
		try {
			Source source = new DOMSource(node);
			StreamResult result = new StreamResult(file);
			Transformer transformer = getTransformerFactory().newTransformer();
			if(omitXMLDeclaration)
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,ENABLED);
			transformer.setErrorListener(errorListener);
			transformer.transform(source, result);
		}catch(Exception e) {
			throw new CoreException(e.getMessage(),e);
		}
	}

	
	
	
	// IS
	
	public boolean isDocument(byte[]xml){
		try{
			return this.newDocument(xml)!=null;
		}catch(Exception e){
			return false;
		}
	}
	public boolean isElement(byte[]xml){
		try{
			return this.newElement(xml)!=null;
		}catch(Exception e){
			return false;
		}
	}

	

	
	// ATTRIBUTE
	
	public String getAttributeValue(Node n,String attrName){
		NamedNodeMap att = n.getAttributes();
		if(att!=null){
			Node nA = att.getNamedItem(attrName);
			if(nA!=null)
				return nA.getNodeValue();
		}
		return null;
	}
	
	public void removeAttribute(Attr attr, Element src){
		
		// NOTA:
		//	 attr.getName ritorna anche il prefisso se c'è a differenza di attr.getLocalName
		
		if(javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attr.getNamespaceURI())){
			if(javax.xml.XMLConstants.XMLNS_ATTRIBUTE.equals(attr.getName())){
				src.removeAttributeNS(attr.getNamespaceURI(), attr.getName());
			}
			else if(attr.getNamespaceURI()!=null){
				src.removeAttributeNS(attr.getNamespaceURI(), attr.getLocalName()); // Deve essere usato localName per cxf
			}
		}
		else{
			src.removeAttribute(attr.getName());
		}
	}
	
	public void addAttribute(Attr attr, Element src){
		
		// NOTA:
		//	 attr.getName ritorna anche il prefisso se c'è a differenza di attr.getLocalName
		
		if(javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attr.getNamespaceURI())){
			if(javax.xml.XMLConstants.XMLNS_ATTRIBUTE.equals(attr.getLocalName())){
				src.setAttribute(attr.getName(), attr.getValue());
			}
			else{
				src.setAttributeNS(attr.getNamespaceURI(), attr.getName(), attr.getValue());
			}
		}
		else{
			src.setAttribute(attr.getName(), attr.getValue());
		}
	}


	
	
	
	
	
	// NAMESPACE
	
	public Hashtable<String, String> getNamespaceDeclaration(Node n){
		NamedNodeMap map = n.getAttributes();
		Hashtable<String, String> namespaces = new Hashtable<String, String>();
		if(map!=null){
			for (int i = 0; i < map.getLength(); i++) {
				Node attribute = map.item(i);
				
				if(attribute!=null && (attribute instanceof  Attr) ){
					Attr a = (Attr) attribute;
					
					String prefix = a.getName();
					if(prefix!=null && (prefix.startsWith(javax.xml.XMLConstants.XMLNS_ATTRIBUTE) || prefix.equals(javax.xml.XMLConstants.XMLNS_ATTRIBUTE))){
						if(prefix.contains(":")){
							prefix = prefix.split(":")[1];
						}
						else{
							prefix = "";
						}
						namespaces.put(prefix, a.getValue());
					}
				}
			}
		}
		return namespaces;
	}
	
	public String findNamespaceByPrefix(String prefix,Node node) {
		return this.findNamespaceByPrefix(prefix, node, node);
	}
	public String findNamespaceByPrefix(String prefix,Node node, Node parentNode) {
		if(node==null) {
			return null;
		}
		
		// cerco tra le definizioni del nodo
		NamedNodeMap nn = node.getAttributes();
		if(nn!=null && nn.getLength()>0) {
			for (int k = 0; k < nn.getLength(); k++) {
				Node nAttr = nn.item(k);
				if(nAttr instanceof Attr) {
					Attr attr = (Attr) nAttr;
					String prefixAttr = attr.getName();
					if(prefixAttr!=null && (prefixAttr.startsWith(javax.xml.XMLConstants.XMLNS_ATTRIBUTE) || prefix.equals("prefixAttr"))){
						if(prefixAttr.contains(":")){
							prefixAttr = prefixAttr.split(":")[1];
						}
						else{
							prefixAttr = "";
						}
					}
					if(prefix.equals(prefixAttr)) {
						return attr.getValue();
					}
				}
			}
		}
		
		// Sono al padre non salgo ulteriormente
		if(node.isSameNode(parentNode)) {
			return null;
		}
		// cerco nel padre a meno che non ho raggiunto il parentNode
		Node p = node.getParentNode();
		if(p==null) {
			return null;
		}

		return this.findNamespaceByPrefix(prefix, p, parentNode);
	}
	
	public void addNamespaceDeclaration(Hashtable<String, String> namespace, Element destNode){
		if(namespace!=null && namespace.size()>0){
			Hashtable<String, String> declarationNamespacesDestNode = this.getNamespaceDeclaration(destNode);
			Enumeration<String> decSource = namespace.keys();
			while (decSource.hasMoreElements()) {
				String dec = (String) decSource.nextElement();
				if(declarationNamespacesDestNode.containsKey(dec)==false){
					String name = "xmlns:"+dec;
					if("".equals(dec)){
						name = javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
					}
					destNode.setAttributeNS(javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI,name,namespace.get(dec));
				}
			}
		}
	}
	
	public void removeNamespaceDeclaration(Hashtable<String, String> namespace, Element destNode){
		
		if(namespace!=null && namespace.size()>0){
			Enumeration<String> decSource = namespace.keys();
			while (decSource.hasMoreElements()) {
				String dec = (String) decSource.nextElement();
				String name = "xmlns:"+dec;
				if("".equals(dec)){
					name = javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
				}
				destNode.removeAttributeNS(javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI, name);
			}
		}
	}
	
	
	
	
	
	
	// NAMESPACE XSI TYPE
	
	private static final String XMLSCHEMA_INSTANCE_LOCAL_NAME_TYPE = "type";
	
	public void addNamespaceXSITypeIfNotExists(Node node, DynamicNamespaceContext dnc, boolean deep) {
		this.addNamespaceXSITypeIfNotExists(node, dnc, deep, node);
	}
	private void addNamespaceXSITypeIfNotExists(Node node, DynamicNamespaceContext dnc, boolean deep, Node parentNode) {
		
		if(node==null) {
			return;
		}
		
		
		
		// Gestisco attributi
		if(node instanceof Element) {
			Element element = (Element) node;
			NamedNodeMap nn = element.getAttributes();
			if(nn!=null && nn.getLength()>0) {
				List<String> prefixXSIType = new ArrayList<>();
				for (int k = 0; k < nn.getLength(); k++) {
					Node nAttr = nn.item(k);
					//System.out.println("ATTR ["+nAttr.getClass().getName()+"]");
					if(nAttr instanceof Attr) {
						Attr attr = (Attr) nAttr;
						//System.out.println("\t name["+attr.getName()+"] value["+attr.getValue()+"] namespace["+attr.getNamespaceURI()+"] prefix["+attr.getPrefix()+"]");
						// name[xsi:type] value[ns10:XXXXType] namespace[http://www.w3.org/2001/XMLSchema-instance] prefix[xsi]
						if(javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(attr.getNamespaceURI())) {
							String prefix = attr.getPrefix();
							String nameAtteso = prefix+":"+XMLSCHEMA_INSTANCE_LOCAL_NAME_TYPE;
							if(prefix==null || "".equals(prefix)) {
								nameAtteso = XMLSCHEMA_INSTANCE_LOCAL_NAME_TYPE;
							}
							if(nameAtteso.equals(attr.getName())) {
								if(attr.getValue()!=null && attr.getValue().contains(":")) {
									prefixXSIType.add(attr.getValue());
								}
							}
						}
					}
				}
				if(prefixXSIType.size()>0) {
					for (String prefixValue : prefixXSIType) {
						String [] tmp = prefixValue.split(":");
						if(tmp!=null && tmp.length==2) {
							String prefix = tmp[0];
							//String localName = tmp[1];
							
//							System.out.println("\n\n\n =============================================================");
//							System.out.println("[{"+node.getNamespaceURI()+"}"+node.getLocalName()+"] prefix ["+prefix+"]");
//							System.out.println("=============================================================\n");
							
							//System.out.println("VERIFICO PRESENZA NAMESPACE PER ["+prefixValue+"] prefix["+prefix+"] localName["+localName+"]");
							String namespaceEsistente = this.findNamespaceByPrefix(prefix, element, parentNode);
							boolean foundNamespace = namespaceEsistente!=null;
							if(!foundNamespace) {
								String namespace = dnc.getNamespaceURI(prefix);
//								System.out.println("[{"+node.getNamespaceURI()+"}"+node.getLocalName()+"] NAMESPACE AGGIUNTO PERCHE' NON TROVATO per ["+prefix+"]=["+namespace+"]");
								element.setAttributeNS(javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:"+prefix, namespace);
							}
//							else {
//								System.out.println("[{"+node.getNamespaceURI()+"}"+node.getLocalName()+"] ESISTE NAMESPACE ["+namespaceEsistente+"] PER PREFIX: "+prefix);
//							}
						}
					}
				}
			}
		}
		
		// Vado in ricorsione sugli elementi figli
		if(deep) {
			List<Node> childList = this.getNotEmptyChildNodes(node, false);
			if(childList!=null && childList.size()>0) {
				for (Node child : childList) {
					this.addNamespaceXSITypeIfNotExists(child, dnc, deep, parentNode);
				}
			}
		}
	}
	

	
	
	
	
	
	
	
	// UTILITIES
	
	public List<Node> getNotEmptyChildNodes(Node e){
		return getNotEmptyChildNodes(e, true);
	}
	public List<Node> getNotEmptyChildNodes(Node e, boolean consideraTextNotEmptyAsNode){
		NodeList nl = e.getChildNodes();
		List<Node> vec = new ArrayList<Node>();
		if(nl!=null){
			for(int index = 0 ; index<nl.getLength(); index++){
				Node n = nl.item(index);
				if(n instanceof Text){
					if(consideraTextNotEmptyAsNode){
						if (((Text) nl.item(index)).getData().trim().length() == 0) { 
							continue;
						}
					}else{
						continue;
					}
				}
				else if (n instanceof Comment) { 
					continue;
				}
				vec.add(nl.item(index));
			}
		}
		return vec;
	}
	
	public Node getFirstNotEmptyChildNode(Node e){
		return getFirstNotEmptyChildNode(e, true);
	}
	public Node getFirstNotEmptyChildNode(Node e, boolean consideraTextNotEmptyAsNode){
		NodeList nl = e.getChildNodes();
		if(nl!=null){
			for(int index = 0 ; index<nl.getLength(); index++){
				Node n = nl.item(index);
				if(n instanceof Text){
					if(consideraTextNotEmptyAsNode){
						if (((Text) nl.item(index)).getData().trim().length() == 0) { 
							continue;
						}
					}else{
						continue;
					}
				}
				else if (nl.item(index) instanceof Comment) { 
					continue;
				}
				return nl.item(index);
			}
		}
		return null;
	}
	
	public boolean matchLocalName(Node nodo,String nodeName,String prefix,String namespace){
		if(nodo==null)
			return false;
		if(nodo.getNodeName()==null)
			return false;
		// Il nodo possiede il prefisso atteso
		if(nodo.getNodeName().equals(prefix+nodeName))
			return true;
		// Il nodo puo' ridefinire il prefisso ridefinendo il namespace
		String namespaceNodo = nodo.getNamespaceURI();
		if(namespaceNodo!=null && namespaceNodo.equals(namespace)){
			String xmlns = nodo.getPrefix();
			if(xmlns == null){ 
				xmlns = "";
			}else if(!xmlns.equals("")){
				xmlns = xmlns + ":";
			}
			if(nodo.getNodeName().equals(xmlns+nodeName))
				return true;
		}
		return false;
	} 
	
	public Node getAttributeNode(Node node,String attributeName){
		if (node == null)
		{
			return null;
		}
		NamedNodeMap map = node.getAttributes();
		if(map==null || map.getLength()==0){
			return null;
		}
		else{
			return map.getNamedItem(attributeName);
		}
	}
	public Node getQualifiedAttributeNode(Node node,String attributeName,String namespace){
		if (node == null)
		{
			return null;
		}
		NamedNodeMap map = node.getAttributes();
		if(map==null || map.getLength()==0){
			return null;
		}
		else{
			return map.getNamedItemNS(namespace, attributeName);
		}
	}
	
}
