package org.gov4j.commons.xml;



import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;


public enum XPathReturnType {

	STRING (XPathConstants.STRING),
	NUMBER (XPathConstants.NUMBER),
	BOOLEAN (XPathConstants.BOOLEAN),
	NODE (XPathConstants.NODE),
	NODESET (XPathConstants.NODESET);

	private final QName valore;

	XPathReturnType(QName valore)
	{
		this.valore = valore;
	}

	public QName getValore()
	{
		return this.valore;
	}

	public static String[] toStringArray(){
		String[] res = new String[XPathReturnType.values().length];
		int i=0;
		for (XPathReturnType tmp : XPathReturnType.values()) {
			res[i]=tmp.getValore().getLocalPart();
			i++;
		}
		return res;
	}
	
	public static String[] toEnumNameArray(){
		String[] res = new String[XPathReturnType.values().length];
		int i=0;
		for (XPathReturnType tmp : XPathReturnType.values()) {
			res[i]=tmp.name();
			i++;
		}
		return res;
	}

	
	@Override
	public String toString(){
		return this.valore.toString();
	}
	
	public boolean equalsReturnType(XPathReturnType esito){
		if(esito == null) return false;
		if(esito instanceof XPathReturnType) {
			return this.toString().equals(esito.toString());
		} else {
			return false;
		}
	}

}

