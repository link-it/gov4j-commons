package org.gov4j.commons.xml;

import org.w3c.dom.Node;

public class DynamicNamespaceContextFactory {

	public static DynamicNamespaceContext getNamespaceContext(Node node)
	{
		DynamicNamespaceContext dnc = new DynamicNamespaceContext();
		dnc.findPrefixNamespace(node);
		return dnc;
	}
	
}
