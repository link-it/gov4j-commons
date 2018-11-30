package org.gov4j.commons.api.http;

import javax.servlet.http.HttpServletResponse;

public class HttpUtilities {

	public final static String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	public final static String HEADER_ATTACH_FILE = "attachment; filename=";
	
	public static void setOutputFile(HttpServletResponse response, boolean noCache, String fileName, String mimeType) {
				
		if(mimeType!=null){
			response.setContentType(mimeType);
		}

		response.setHeader(HttpConstants.CONTENT_DISPOSITION, (new StringBuilder()).append(HttpConstants.CONTENT_DISPOSITION_ATTACH_FILE_PREFIX+"\"").append(fileName).append("\"").toString());
		
		// no cache
		if(noCache){
			setNoCache(response);
		}
		
	}
	
	
	
	public static void setNoCache(HttpServletResponse response) {
		response.setHeader(HttpConstants.CACHE_STATUS_HTTP_1_1, HttpConstants.CACHE_STATUS_HTTP_1_1_DISABLE_CACHE); // HTTP 1.1.
		response.setHeader(HttpConstants.CACHE_STATUS_HTTP_1_0, HttpConstants.CACHE_STATUS_HTTP_1_0_DISABLE_CACHE); // HTTP 1.0.
		response.setDateHeader(HttpConstants.CACHE_STATUS_PROXY_EXPIRES, HttpConstants.CACHE_STATUS_PROXY_EXPIRES_DISABLE_CACHE); // Proxies.
	}
	
}
