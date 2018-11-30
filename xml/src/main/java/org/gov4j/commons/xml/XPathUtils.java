package org.gov4j.commons.xml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;

import org.gov4j.commons.core.exception.CoreException;
import org.gov4j.commons.core.exception.ExceptionUtilities;
import org.gov4j.commons.core.exception.NotFoundException;
import org.gov4j.commons.core.exception.UtilsMultiException;
import org.gov4j.commons.core.exception.ValidationException;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathUtils {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(XPathUtils.class);
	public static void setLogger(Logger logger) {
		XPathUtils.logger = logger;
	}
	
	private static XPathUtils xPathUtils = null;
	private static synchronized void init(){
		if(XPathUtils.xPathUtils==null){
			XPathUtils.xPathUtils = new XPathUtils();
		}
	}
	public static XPathUtils getInstance(){
		if(XPathUtils.xPathUtils==null){
			XPathUtils.init();
		}
		return XPathUtils.xPathUtils;
	}	
	
	
	/* ---------- METODI RITORNANO STRINGHE -------------- */
	
	
	public String getStringMatchPattern(Document document, String pattern) 
			throws CoreException,NotFoundException,ValidationException{
		return this.getStringMatchPattern(document, DynamicNamespaceContextFactory.getNamespaceContext(document.getDocumentElement()), pattern);
	}
	public String getStringMatchPattern(Document document, DynamicNamespaceContext dnc,String pattern) 
		throws CoreException,NotFoundException,ValidationException{
		return (String) this.getMatchPattern(document.getDocumentElement(), dnc, pattern, XPathReturnType.STRING);
	}
	
	public String getStringMatchPattern(Element element, String pattern) 
		throws CoreException,NotFoundException,ValidationException{
		return this.getStringMatchPattern(element, DynamicNamespaceContextFactory.getNamespaceContext(element), pattern);
	}
	public String getStringMatchPattern(Element element, DynamicNamespaceContext dnc,String pattern) 
		throws CoreException,NotFoundException,ValidationException{
		return (String) this.getMatchPattern(element, dnc, pattern, XPathReturnType.STRING);
	}
	
	public String getStringMatchPattern(String contenuto, DynamicNamespaceContext dnc,String pattern) 
		throws CoreException,NotFoundException,ValidationException{
		return (String) this.getMatchPattern(contenuto, dnc, pattern, XPathReturnType.STRING);
	}
	
	
	
	/* ---------- METODI GENERICI -------------- */

	public Object getMatchPattern(Document document, String pattern, XPathReturnType returnType) 
			throws CoreException,NotFoundException,ValidationException{
		return this.getMatchPattern(document, DynamicNamespaceContextFactory.getNamespaceContext(document.getDocumentElement()), pattern, returnType);
	}
	public Object getMatchPattern(Document document, DynamicNamespaceContext dnc,String pattern, XPathReturnType returnType) 
		throws CoreException,NotFoundException,ValidationException{
		
		if(document==null)
			throw new CoreException("document xml undefined");
		if(document.getDocumentElement()==null)
			throw new CoreException("document.element xml undefined");
		
		return this._engine_getMatchPattern(document.getDocumentElement(), null, dnc, pattern, returnType);
		
	}
	
	public Object getMatchPattern(Element element, String pattern, XPathReturnType returnType) 
			throws CoreException,NotFoundException,ValidationException{
		return this.getMatchPattern(element, DynamicNamespaceContextFactory.getNamespaceContext(element), pattern, returnType);
	}
	public Object getMatchPattern(Element element, DynamicNamespaceContext dnc,String pattern, XPathReturnType returnType) 
		throws CoreException,NotFoundException,ValidationException{
		
		if(element==null)
			throw new CoreException("element xml undefined");
		
		return this._engine_getMatchPattern(element, null, dnc, pattern, returnType);
	}
	
	public Object getMatchPattern(String content, DynamicNamespaceContext dnc,String pattern, XPathReturnType returnType) 
		throws CoreException,NotFoundException,ValidationException{
		
		if(content==null)
			throw new CoreException("content undefined");
		
		return this._engine_getMatchPattern(null, content, dnc, pattern, returnType);
		
	}
	
	
	
	/* ---------- ENGINE -------------- */
	
	private static final Object synchronizedObjectForBugFWK005ParseXerces = new Object();
	private Object _engine_getMatchPattern(
			Element contenutoAsElement, String contenutoAsString, 
			DynamicNamespaceContext dncPrivate,String pattern, XPathReturnType returnType) 
			throws CoreException,NotFoundException,ValidationException{

		if( (pattern == null) || (pattern.length() == 0))
			throw new NotFoundException("Pattern di ricerca non fornito");
		if(contenutoAsElement == null && contenutoAsString==null)
			throw new NotFoundException("Contenuto su cui effettuare la ricerca non fornito");
		if(contenutoAsElement != null && contenutoAsString!=null)
			throw new NotFoundException("Contenuto su cui effettuare la ricerca ambiguo");

		
		// Validazione espressione XPAth fornita
		pattern = pattern.trim();
		this.validate(pattern);	
		
		XMLUtils xmlUtils = XMLUtils.getInstance();
		
		try{
			
			// 1. Instantiate an XPathFactory.
			javax.xml.xpath.XPathFactory factory = xmlUtils.getXPathFactory();
	
			// 2. Use the XPathFactory to create a new XPath object
			javax.xml.xpath.XPath xpath = factory.newXPath();
			if(xpath==null){
				throw new Exception("Costruzione xpath non riuscita");
			}
			
			// 3. Set the Namespaces
			
			// 3.1. Clone dnc, verra' modificato in convertNamespaces
			DynamicNamespaceContext dnc = null;
			if(dncPrivate!=null){
				dnc = (DynamicNamespaceContext) dncPrivate.clone();
			}
			
			// 3.2 // Risoluzione namespace {http:///}
			// NOTA: da utilizzare con {http://namespace}localName 
			// BugFix: se per errore viene utilizzato {http://namespace}:localName prima di effettuare la risoluzione del namespace viene eliminato il ':'
			if(dnc!=null){
				pattern = pattern.replaceAll("}:", "}");
				pattern = this.convertNamespaces(pattern, dnc);
			}
			//System.out.println("PATTERN: ["+pattern+"]");

			// 3.3 set dnc in xpath
			if(dnc!=null)
				xpath.setNamespaceContext(dnc);
			
			// 3.4
			// Bug fix: se il contenuto possiede una namespace associato al prefix di default, poi l'xpath engine non trova l'elemento.
			// es. <prova xmlns="www.namespace">TEST</prova> con xpath /prova/text() non funziona.
//			if(dnc.getNamespaceURI(javax.xml.XMLConstants.DEFAULT_NS_PREFIX).equals(javax.xml.XMLConstants.NULL_NS_URI) == false ){
//				//System.out.println("PATCH CONTENUTO PER NAMESPACE DEFAULT: "+dnc.getNamespaceURI(javax.xml.XMLConstants.DEFAULT_NS_PREFIX));
//				contenuto = contenuto.replace("xmlns=\""+dnc.getNamespaceURI(javax.xml.XMLConstants.DEFAULT_NS_PREFIX)+"\"", "");
//			}	
			// !!!! NOTA Il fix sopra indicato non era corretto.
			// e' giusto che non lo trova se viene fornito un xpath senza prefisso, poiche' si sta cercando un elemento che non appartiene a nessun namespace.
			// L'xpath sopra funzionera' su un xml definito come <prova>TEST</prova>
			//
			// Ulteriore spiegazione:
			// But since the nodes you are trying to get use a default namespace, without a prefix, using plain XPath, 
			// you can only access them by the local-name() and namespace-uri() attributes. Examples:
			// *[local-name()="HelloWorldResult"]/text()
			// Or:
			// *[local-name()="HelloWorldResult" and namespace-uri()='http://tempuri.org/']/text()
			// Or:
			//*[local-name()="HelloWorldResponse" and namespace-uri()='http://tempuri.org/']/*[
			//
			// Siccome la modalita' di utilizzare il local-name e il namespace-uri in un xpath e' elaborioso, abbiamo aggiunto in openspcoop2
			// la possibilita' di indicare il namespace {namespace} che verra' convertito in un prefix interno utile al funzionamento dell'xpath.
			// Il pattern da fornire  che funziona sull'xml indicato sopra e' /{www.namespace}prova/text()
			// Per ulteriori dettagli vedi metodo convertNamespaces utilizzato in 3.2, all'interno del metodo viene fornita una descrizione dettagliata.
			
				
			// Concatenazione openspcoop.
			if(pattern.startsWith("concat_openspcoop") && pattern.endsWith(")")){
				
				// Check compatibilita' concat_openspcoop
				if(returnType.equalsReturnType(XPathReturnType.STRING)==false){
					throw new CoreException("Funzione concat_openspcoop non compatibile con un tipo di ritorno: "+returnType.toString());
				}
				
				// Fix , la funzione concat non ritorna eccezione se non riesce a risolvere qualche espressione, e contiene cmq delle costanti.
				// La concatenazione openspcoop, invece ritornera' errore.
				String param = pattern.substring("concat_openspcoop(".length(),pattern.length()-1);
				String [] params =param.split(",");
				StringBuffer bfResult = new StringBuffer();
				for(int i=0; i<params.length;i++){
					
					// Check se abbiamo una costante od una espressione da valutare
					if(params[i].startsWith("\"") && params[i].endsWith("\"")){
					
						// COSTANTE
						bfResult.append(params[i].substring(1,(params[i].length()-1)));
					
					}else{
						
						// 4. Reader
						Reader reader = null;
						if(contenutoAsString!=null){
							try{
								reader = new StringReader(contenutoAsString);
							}catch(Exception e){
								try{
									if( reader != null )
										reader.close();
								} catch(Exception er) {}
								throw e;
							}		
						}
						
						// ESPRESSIONE
						// 5. Compile an XPath string into an XPathExpression
						javax.xml.xpath.XPathExpression expression = null;
						try{
							expression = xpath.compile(params[i]);
						}catch(Exception e){
							if(ExceptionUtilities.existsInnerMessageException(e, "Prefix must resolve to a namespace", true)){
								// e' stato usato un prefisso nell'espressione XPath che non e' risolvibile accedendo ai namespaces del messaggio (dnc)
								throw new NotFoundException("Espressione XPATH contenuta in concat_openspcoop ("+params[i]+") non applicabile al messaggio: "+ExceptionUtilities.getInnerMessageException(e, "Prefix must resolve to a namespace", true));
							}
							else if(ExceptionUtilities.existsInnerException(e,javax.xml.transform.TransformerException.class)){
								throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+ExceptionUtilities.getInnerException(e, javax.xml.transform.TransformerException.class).getMessage()+")",e);
							}else{
								if(e.getCause()!=null){
									throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
								}else{
									throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+e.getMessage()+")",e);
								}
							}	
						}
						if(expression==null){
							throw new Exception("Costruzione XPathExpression non riuscita per espressione contenuta in concat_openspcoop ("+params[i]+")");
						}
				
												
						// 6. Evaluate the XPath expression on an input document
						String result = null;
						try{
							synchronized (XPathUtils.synchronizedObjectForBugFWK005ParseXerces) {
								if(reader!=null)
									result = expression.evaluate(new org.xml.sax.InputSource(reader));
								else 
									result = expression.evaluate(contenutoAsElement);
							}
						}catch(Exception e){
							if(ExceptionUtilities.existsInnerException(e,"com.sun.org.apache.xpath.internal.CoreException")){
								throw new Exception("Valutazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+ExceptionUtilities.getInnerException(e, "com.sun.org.apache.xpath.internal.CoreException").getMessage()+")",e);
							}
							else if(ExceptionUtilities.existsInnerException(e,"org.apache.xpath.CoreException")){
								throw new Exception("Valutazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+ExceptionUtilities.getInnerException(e, "org.apache.xpath.CoreException").getMessage()+")",e);
							}
							else{
								if(e.getCause()!=null){
									throw new Exception("Valutazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
								}else{
									throw new Exception("Valutazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+e.getMessage()+")",e);
								}
							}
						}
						if(reader!=null)
							reader.close();
				
						if(result == null || "".equals(result)){
							// DEVE ESSERE LANCIATA ECCEZIONE, E' PROPRIO LA FUNZIONALITA DI OPENSPCOOP!
							// Il Concat normale non lancia eccezione e ritorna stringa vuota.
							// Verificato con cxf soap engine
							logger.debug("nessun match trovato per l'espressione xpath contenuta in concat_openspcoop ("+params[i]+")");
							throw new NotFoundException("nessun match trovato per l'espressione xpath contenuta in concat_openspcoop ("+params[i]+")");
						}
						else{
							bfResult.append(result);
						}
					}
				}
				
				if(bfResult.length()<=0){
					StringBuffer bfDNC = new StringBuffer();
					Enumeration<?> en = dnc.getPrefixes();
					while (en.hasMoreElements()) {
						String prefix = (String) en.nextElement();
						bfDNC.append("\n\t- ");
						bfDNC.append("[").append(prefix).append("]=[").append(dnc.getNamespaceURI(prefix)).append("]");
					}
					throw new NotFoundException("nessun match trovato per l'espressione xpath (concat_openspcoop) ["+pattern+"] DynamicNamespaceContext:"+bfDNC.toString());
					
				}else{
					return bfResult.toString();
				}

			}else{
				
				// 4. Reader
				Reader reader = null;
				if(contenutoAsString!=null){
					try{
						reader = new StringReader(contenutoAsString);
					}catch(Exception e){
						try{
							if( reader != null )
								reader.close();
						} catch(Exception er) {}
						throw e;
					}		
				}
				
				
				// 5. Compile an XPath string into an XPathExpression
				javax.xml.xpath.XPathExpression expression = null;
				try{
					expression = xpath.compile(pattern);
				}catch(Exception e){
					if(ExceptionUtilities.existsInnerMessageException(e, "Prefix must resolve to a namespace", true)){
						// e' stato usato un prefisso nell'espressione XPath che non e' risolvibile accedendo ai namespaces del messaggio (dnc)
						throw new NotFoundException("Espressione XPATH non applicabile al messaggio: "+ExceptionUtilities.getInnerMessageException(e, "Prefix must resolve to a namespace", true));
					}
					else if(ExceptionUtilities.existsInnerException(e,javax.xml.transform.TransformerException.class)){
						throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+ExceptionUtilities.getInnerException(e, javax.xml.transform.TransformerException.class).getMessage()+")",e);
					}else{
						if(e.getCause()!=null){
							throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
						}else{
							throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+e.getMessage()+")",e);
						}
					}
				}
				if(expression==null){
					throw new Exception("Costruzione XPathExpression non riuscita");
				}
		
				// 6. Evaluate the XPath expression on an input document
				Object result = null;
				try{
					synchronized (XPathUtils.synchronizedObjectForBugFWK005ParseXerces) {
						if(reader!=null)
							result = expression.evaluate(new org.xml.sax.InputSource(reader),returnType.getValore());
						else{
							result = expression.evaluate(contenutoAsElement,returnType.getValore());
						}
					}
				}catch(Exception e){
					if(ExceptionUtilities.existsInnerException(e,"com.sun.org.apache.xpath.internal.CoreException")){
						throw new Exception("Valutazione dell'espressione XPATH ha causato un errore ("+ExceptionUtilities.getInnerException(e, "com.sun.org.apache.xpath.internal.CoreException").getMessage()+")",e);
					}
					else if(ExceptionUtilities.existsInnerException(e,"org.apache.xpath.CoreException")){
						throw new Exception("Valutazione dell'espressione XPATH ha causato un errore ("+ExceptionUtilities.getInnerException(e, "org.apache.xpath.CoreException").getMessage()+")",e);
					}
					else{
						if(e.getCause()!=null){
							throw new Exception("Valutazione dell'espressione XPATH ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
						}else{
							throw new Exception("Valutazione dell'espressione XPATH ha causato un errore ("+e.getMessage()+")",e);
						}
					}
				}
				if(reader!=null)
					reader.close();
		
				boolean notFound = false;
				if(result == null){
					notFound = true;
				}
				else if(result instanceof String){
					if("".equals(result)){
						notFound = true;
					}
				}
				else if( (XPathReturnType.NODESET.equalsReturnType(returnType)) && (result instanceof NodeList)){
					if(((NodeList)result).getLength()<=0){
						notFound=true;
					}
				}
				
				if(notFound){
					//log.info("ContentBased, nessun match trovato");
					StringBuffer bfDNC = new StringBuffer();
					Enumeration<?> en = dnc.getPrefixes();
					while (en.hasMoreElements()) {
						String prefix = (String) en.nextElement();
						bfDNC.append("\n\t- ");
						bfDNC.append("[").append(prefix).append("]=[").append(dnc.getNamespaceURI(prefix)).append("]");
					}
					throw new NotFoundException("nessun match trovato per l'espressione xpath ["+pattern+"] DynamicNamespaceContext:"+bfDNC.toString());
				}
				
				return result; 
			
			}
		
		}catch(NotFoundException ex){
			throw ex;
		}catch(Exception e){
			throw new CoreException("getMatchPattern pattern["+pattern+"] error: "+e.getMessage(),e);
		}
	} 
	
	
	
	
	
	/* ---------- VALIDATORE -------------- */
	
	public void validate(String path) throws ValidationException{
		try{
			
			path = path.trim();
				
			// Instantiate an XPathFactory.
			javax.xml.xpath.XPathFactory factory = XMLUtils.getInstance().getXPathFactory();
	
			// Use the XPathFactory to create a new XPath object
			javax.xml.xpath.XPath xpath = factory.newXPath();
			if(xpath==null){
				throw new Exception("Costruzione xpath non riuscita");
			}
			
			// 3. Repleace the Namespaces:
			path = path.replaceAll("}:", "}");
			int index = 0;
			if(path.indexOf("{")!=-1){
				while (path.indexOf("{")!=-1){
					int indexStart = path.indexOf("{");
					int indexEnd = path.indexOf("}");
					if(indexEnd==-1){
						throw new Exception("{ utilizzato senza la rispettiva chiusura }");
					}
					String namespace = path.substring(indexStart+"{".length(),indexEnd);
					if(namespace==null || namespace.equals("")){
						throw new Exception("Namespace non indicato tra {}");
					}
					String prefix = "a"+index+":";
					index++;
					path = path.replace("{"+namespace+"}", prefix);
				}
			}
			//System.out.println("PATTERN PER VALIDAZIONE: ["+path+"]");
			
			if(path.startsWith("concat_openspcoop")){
				// funzione concat
				String param = path.substring("concat_openspcoop(".length(),path.length()-1);
				String [] params =param.split(",");
				for(int i=0; i<params.length;i++){
					
					// Check se abbiamo una costante od una espressione da valutare
					if(params[i].startsWith("\"") && params[i].endsWith("\"")){
					
						// COSTANTE
					
					}else{
						
						javax.xml.xpath.XPathExpression expression = null;
						try{
							expression = xpath.compile(params[i]);
						}catch(Exception e){
							if(ExceptionUtilities.existsInnerException(e,javax.xml.transform.TransformerException.class)){
								throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+ExceptionUtilities.getInnerException(e, javax.xml.transform.TransformerException.class).getMessage()+")",e);
							}else{
								if(e.getCause()!=null){
									throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
								}else{
									throw new Exception("Compilazione dell'espressione XPATH contenuta in concat_openspcoop ("+params[i]+") ha causato un errore ("+e.getMessage()+")",e);
								}
							}			
						}
						if(expression==null){
							throw new Exception("Costruzione XPathExpression non riuscita per espressione contenuta in concat_openspcoop ("+params[i]+")");
						}
						
					}
				}
			}
			else{
				// Compile an XPath string into an XPathExpression
				javax.xml.xpath.XPathExpression expression = null;
				try{
					expression = xpath.compile(path);
				}catch(Exception e){
					if(ExceptionUtilities.existsInnerException(e,javax.xml.transform.TransformerException.class)){
						throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+ExceptionUtilities.getInnerException(e, javax.xml.transform.TransformerException.class).getMessage()+")",e);
					}else{
						if(e.getCause()!=null){
							throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+(ExceptionUtilities.getLastInnerException(e.getCause())).getMessage()+")",e);
						}else{
							throw new Exception("Compilazione dell'espressione XPATH ha causato un errore ("+e.getMessage()+")",e);
						}
					}	
				}
				if(expression==null){
					throw new Exception("Costruzione XPathExpression non riuscita");
				}
			}
		}catch(Exception e){
			throw new ValidationException("Validazione dell'xpath indicato ["+path+"] fallita: "+e.getMessage(),e);
		}
	}
	
	
	
	
	
	
	
	
	
	/* -------------- UTILITIES PUBBLICHE ------------------- */
	
	/**
	 * Ritorna la rappresentazione testuale di una lista di nodi
	 * 
	 * @param rootNode
	 * @return rappresentazione testuale di una lista di nodi
	 */
	public String toString(NodeList rootNode){
		
		StringBuffer resultBuffer = new StringBuffer();
		this.toString(rootNode,resultBuffer,1);
		return resultBuffer.toString();
		
	}
	
	
	/* -------------- UTILITIES PRIVATE ------------------- */
	private int _prefixIndex = 0;
	private synchronized int _getNextPrefixIndex(){
		this._prefixIndex++;
		return this._prefixIndex;
	}
	private final static String AUTO_PREFIX = "_opPrefixAutoGeneratedIndex";
	private String convertNamespaces(String path,DynamicNamespaceContext dnc)throws CoreException{
		if(path.indexOf("{")!=-1){
			while (path.indexOf("{")!=-1){
				int indexStart = path.indexOf("{");
				int indexEnd = path.indexOf("}");
				if(indexEnd==-1){
					throw new CoreException("Errore durante l'interpretazione del valore ["+path+"]: { utilizzato senza la rispettiva chiusura }");
				}
				String namespace = path.substring(indexStart+"{".length(),indexEnd);
				String prefix = dnc.getPrefix(namespace);
				//System.out.println("PREFIX["+prefix+"]->NS["+namespace+"]");
				if(prefix!=null && !javax.xml.XMLConstants.NULL_NS_URI.equals(prefix)){
					prefix = prefix+":";
				}
				else{
					// aggiungo un prefisso nuovo anche nel caso di prefix "" per gestire la problematica che si presente in questo caso:
					// String xmlCampione = "<prova xmlns=\"www.namespace\">TEST</prova>";
					// NON FUNZIONA con un pattern = "/prova/text()"; (poiche' l'elemento prova ha un namespace, quello di default, mentre nell'xpath non viene indicato)
					// 				Questo pattern sarebbe quello generato anche dopo la conversione dei namespace se il prefisso e' javax.xml.XMLConstants.NULL_NS_URI
					// FUNZIONA con un pattern = "*[local-name()='prova' and namespace-uri()='www.namespace']/text()"; ma e' piu' elaborato
					// FUNZIONA se registriamo un nuovo prefix da utilizzare solo nel pattern, ad esempio con un pattern = "/_op2PrefixAutoGeneratedIndexNUMBER:prova/text()"
					//			dove il prefisso auto-generato _op2PrefixAutoGeneratedIndexNUMBER e' associato al namespace www.namespace nel dnc.
					prefix = AUTO_PREFIX + this._getNextPrefixIndex();
					dnc.addNamespace(prefix, namespace);
					prefix = prefix+":";
					//System.out.println("CASO SPECIALEEEEEEEEEEEEEEEE");
				}
				path = path.replace("{"+namespace+"}", prefix);
			}
		}
		return path;
	}
	
	private void toString(NodeList rootNode,StringBuffer resultBuffer, int livello){
		
		if (rootNode.getLength()==1 && rootNode.item(0).getNodeType()==Node.TEXT_NODE && resultBuffer.length()==0){
			//System.out.println("TEXT ["+rootNode.item(0).getNodeType()+"] confronto con ["+Node.TEXT_NODE+"]");
			resultBuffer.append(rootNode.item(0).getTextContent());
			return;
		}
		boolean findElementoRisultatoMultiplo = false;
		for(int index = 0; index < rootNode.getLength();index ++){
			Node aNode = rootNode.item(index);
			//System.out.println("NODE["+index+"] ["+aNode.getNodeType()+"] confronto con  ["+Node.ELEMENT_NODE+"]");
			if (aNode.getNodeType() == Node.ELEMENT_NODE){
				NodeList childNodes = aNode.getChildNodes(); 
				
				if (childNodes.getLength() > 0){
					
					boolean hasChildNodes = false;
					for(int i=0;i<childNodes.getLength();i++){
						// elimino i text node che possono contenere "\n" con axiom
						Node n = childNodes.item(i);
						if(n.hasChildNodes()){
							hasChildNodes = true;
							break;
						}
					}
					
					if (hasChildNodes){
						resultBuffer.append("<"+aNode.getNodeName());

						this.printAttributes(aNode,resultBuffer);

						resultBuffer.append(">");
						
						this.toString(aNode.getChildNodes(), resultBuffer, (livello+1));
						resultBuffer.append("</"+aNode.getNodeName()+">");
					}
					else {
						resultBuffer.append("<"+aNode.getNodeName());
						
						this.printAttributes(aNode,resultBuffer);
						
						resultBuffer.append(">"+aNode.getTextContent()+ "</"+aNode.getNodeName()+">");
					}
				}
			}
			// Risultati multipli per uno stesso elemento
			else if ( (livello==1) && (aNode.getNodeType() == Node.TEXT_NODE) ){
				if(findElementoRisultatoMultiplo){
					resultBuffer.append(", ");
				}
				else {
					findElementoRisultatoMultiplo = true;
				}
				resultBuffer.append("["+index+"]="+aNode.getTextContent());
			}
		}
	}
	
	private void printAttributes(Node aNode,StringBuffer resultBuffer){
		NamedNodeMap attr = aNode.getAttributes();
		for (int i=0;i<attr.getLength();i++){
			Node item = attr.item(i);
			if(item instanceof Attr){
				Attr attribute = (Attr) item;
				//System.out.println("PREFIX["+attribute.getPrefix()+"] LOCALNAME=["+attribute.getLocalName()+"] NAMESPACE["+attribute.getNodeValue()+"]");
				String prefix = attribute.getPrefix();
				if(prefix!=null && !"".equals(prefix)){
					prefix = prefix + ":";
				}else{
					prefix = "";
				}
				String value = attribute.getNodeValue();
				resultBuffer.append(" "+prefix+attribute.getLocalName()+"=\""+value+"\"");
			}else{
				resultBuffer.append(" "+item.toString());
			}
		}
	}
	
	
	
	public static String extractAndConvertResultAsString(Element element,String pattern, Logger log) throws Exception {
		DynamicNamespaceContext dnc = new DynamicNamespaceContext();
		dnc.findPrefixNamespace(element);
		return extractAndConvertResultAsString(element, dnc, pattern, log);
	}
	public static String extractAndConvertResultAsString(Element element,DynamicNamespaceContext dnc,String pattern, Logger log) throws Exception {
		
		XPathUtils xPathEngine = XPathUtils.getInstance();
		
		// Provo a cercarlo prima come Node
		NodeList nodeList = null;
		Exception exceptionNodeSet = null;
		String risultato = null;
		try{
			nodeList = (NodeList) xPathEngine.getMatchPattern(element, dnc, pattern,XPathReturnType.NODESET);
			if(nodeList!=null){
				risultato = xPathEngine.toString(nodeList);
			}
		}catch(Exception e){
			exceptionNodeSet = e;
		}
		
		// Se non l'ho trovato provo a cercarlo come string (es. il metodo sopra serve per avere l'xml, ma fallisce in caso di concat, in caso di errori di concat_openspcoop....)
		// Insomma il caso dell'xml sopra e' quello speciale, che pero' deve essere eseguito prima, perche' altrimenti il caso string sotto funziona sempre, e quindi non si ottiene mai l'xml.
		if(risultato==null || "".equals(risultato)){
			try{
				risultato = xPathEngine.getStringMatchPattern(element, dnc, pattern);
			}catch(Exception e){
				if(exceptionNodeSet!=null){
					log.debug("Errore avvenuto durante la getStringMatchPattern("+pattern
							+") ("+e.getMessage()+") invocata in seguito all'errore dell'invocazione getMatchPattern("+
							pattern+",NODESET): "+exceptionNodeSet.getMessage(),exceptionNodeSet);
				}
				// lancio questo errore e se presente anche quello con la ricerca notset nodoset.
				if(exceptionNodeSet!=null) {
					throw new UtilsMultiException(e,exceptionNodeSet);
				}
				else {
					throw e;
				}
			}
		}
		
		if(risultato == null || "".equals(risultato)){
			if(exceptionNodeSet!=null){
				log.debug("Non sono stati trovati risultati tramite l'invocazione del metodo getStringMatchPattern("+pattern
						+") invocato in seguito all'errore dell'invocazione getMatchPattern("+
						pattern+",NODESET): "+exceptionNodeSet.getMessage(),exceptionNodeSet);
				// lancio questo errore.
				// Questo errore puo' avvenire perche' ho provato a fare xpath con nodeset
				//throw exceptionNodeSet;
			}
		}
		
		return risultato;
	}
	
}
