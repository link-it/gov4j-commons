package org.gov4j.commons.xml.xsd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class XSDResourceResolver implements LSResourceResolver {

	private Hashtable<String, byte[]> resources = new Hashtable<String, byte[]>();
	
	public Hashtable<String, byte[]> getResources() {
		return this.resources;
	}
	public XSDResourceResolver(){}
	public XSDResourceResolver(Hashtable<String, byte[]> resources){
		this.resources = resources;
	}
	
	public void addResource(String systemId,byte[] resource){
		this.resources.put(systemId, resource);
	}
	
	public void addResource(String systemId,InputStream resource) throws IOException{
		if(resource==null){
			throw new IOException("InputStream is null");
		}
		byte [] buffer = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int letti = 0;
		while( (letti=resource.read(buffer))!=-1 ){
			bout.write(buffer, 0, letti);
		}
		bout.flush();
		bout.close();
		addResource(systemId, bout.toByteArray());
	}
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {
		
		try{
		
			// Algoritmo:
			// 1. Cerco con systemId completo		
			byte[] resource = this.resources.get(systemId);
			
			if(resource==null){
				
				String baseName = null;
				String parentName = null;
				if(systemId.startsWith("http://") || systemId.startsWith("https://") || systemId.startsWith("file://")){
					URL url = new URL(systemId);
					File fileUrl = new File(url.getFile());
					baseName = fileUrl.getName();
					if(fileUrl.getParentFile()!=null){
						parentName = fileUrl.getParentFile().getName();
					}
				}
				else{
					File f = new File(systemId);
					baseName = f.getName();
					if(f.getParentFile()!=null){
						parentName = f.getParentFile().getName();
					}
				}
				
				if(parentName!=null){
					resource = this.resources.get(parentName+"/"+baseName);
				}
				
				if(resource==null){
					resource = this.resources.get(baseName);
				}
				
				if(resource==null){
					if(baseURI!=null){
						String ricerca = null;
						if(baseURI.startsWith("http://") || baseURI.startsWith("file://")){	
							URL url = new URL(baseURI);
							File fileUrl = new File(url.getFile());
							String baseNameParent = fileUrl.getName();
							if(baseURI.length()>baseNameParent.length()){
								String prefix = baseURI.substring(0, baseURI.length()-baseNameParent.length());
								ricerca = prefix + baseName;
							}
						}
						else{
							File f = new File(baseURI);
							if(f.getParentFile()!=null){
								String prefix = f.getParentFile().getAbsolutePath();
								ricerca = prefix + File.separatorChar + baseName;
							}
						}
						//System.out.println("RICERCO COME ["+ricerca+"]");
						resource = this.resources.get(ricerca);
					}
				}
			}
			
			if(resource!=null){
				return new LSInputImpl(type, namespaceURI, publicId, systemId, baseURI, resource);
			}
			else{
				//System.out.println("NON TROVATO");
				throw new Exception("non trovata tra le risorse registrate (type["+type+"] namespaceURI["+namespaceURI+"] publicId["+publicId+"] systemId["+systemId+"] baseURI["+baseURI+"])");
			}
			
		}catch(Exception e){
			throw new RuntimeException("Risoluzione risorsa non riuscita: "+e.getMessage(),e);
		}
		
	}

}
