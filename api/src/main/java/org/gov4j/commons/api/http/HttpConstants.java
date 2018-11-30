package org.gov4j.commons.api.http;

public class HttpConstants {
	
	protected final static byte[] CR_LF_BREAK_LINE = { 13, 10 };
	public final static String HTTP_HEADER_SEPARATOR = ": ";
	
	/** HTTP HEADERS */
	public final static String CONTENT_TYPE = "Content-Type";
	public final static String CONTENT_LENGTH = "Content-Length";
	public final static String CONTENT_LOCATION = "Content-Location";
	public final static String CONTENT_ID = "Content-ID";
	public final static String RETURN_CODE = "ReturnCode";
	
    /** ContentType */
    public final static String CONTENT_TYPE_NON_VALORIZZATO = "Notv alued (null)";
    public final static String CONTENT_TYPE_NON_PRESENTE = "Undefined";
	
	/** Transfer Encoding */
	public final static String TRANSFER_ENCODING = "Transfer-Encoding";
	public final static String TRANSFER_ENCODING_VALUE_CHUNCKED = "chunked";
	
	/** Content Transfer Encoding */
	public final static String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public final static String CONTENT_TRANSFER_ENCODING_VALUE_BINARY = "binary";
	
	/** Redirect */
	public final static String REDIRECT_LOCATION = "Location";
	
	/** Proxy */
	public final static String PROXY_AUTHORIZATION = "Proxy-Authorization";
	
	/** Authorization */
	public final static String AUTHORIZATION = "Authorization";
	public final static String AUTHORIZATION_PREFIX_BASIC = "Basic ";
	public final static String AUTHORIZATION_PREFIX_BEARER = "Bearer ";
	public final static String AUTHORIZATION_RESPONSE_WWW_AUTHENTICATE = "WWW-Authenticate";
	public final static String AUTHORIZATION_RESPONSE_WWW_AUTHENTICATE_BASIC_REALM_PREFIX = "Basic realm=\"\"";
	public final static String AUTHORIZATION_RESPONSE_WWW_AUTHENTICATE_BASIC_REALM_SUFFIX = "\"";
	
	/** Download */
	public final static String CONTENT_DISPOSITION = "Content-Disposition";
	public final static String CONTENT_DISPOSITION_ATTACH_FILE_PREFIX = "attachment; filename=";
	
	/** Cache */
	public final static String CACHE_STATUS_HTTP_1_1 = "Cache-Control";
	public final static String CACHE_STATUS_HTTP_1_1_DISABLE_CACHE = "no-cache, no-store, must-revalidate";
	public final static String CACHE_STATUS_HTTP_1_0 = "Pragma";
	public final static String CACHE_STATUS_HTTP_1_0_DISABLE_CACHE = "no-cache";
	public final static String CACHE_STATUS_PROXY_EXPIRES = "Expires";
	public final static long CACHE_STATUS_PROXY_EXPIRES_DISABLE_CACHE = 0;
	
	/** Agent */
	public final static String USER_AGENT = "User-Agent";
	public final static String SERVER = "Server";
	public final static String X_POWERED_BY = "X-Powered-By";
	
	/** Content Type, Parameter */
	public final static String CONTENT_TYPE_PARAMETER_CHARSET = "charset";
	
	/** Multipart Content Type */
	
	public final static String CONTENT_TYPE_MULTIPART_TYPE = "multipart";
	public final static String CONTENT_TYPE_MULTIPART_ALTERNATIVE_SUBTYPE = "alternative";
	public final static String CONTENT_TYPE_MULTIPART_MIXED_SUBTYPE = "mixed";
	public final static String CONTENT_TYPE_MULTIPART_RELATED_SUBTYPE = "related";
	
	public final static String CONTENT_TYPE_MULTIPART_ALTERNATIVE = CONTENT_TYPE_MULTIPART_TYPE+"/"+CONTENT_TYPE_MULTIPART_ALTERNATIVE_SUBTYPE;
	public final static String CONTENT_TYPE_MULTIPART_MIXED = CONTENT_TYPE_MULTIPART_TYPE+"/"+CONTENT_TYPE_MULTIPART_MIXED_SUBTYPE;
	public final static String CONTENT_TYPE_MULTIPART_RELATED = CONTENT_TYPE_MULTIPART_TYPE+"/"+CONTENT_TYPE_MULTIPART_RELATED_SUBTYPE;
	
	public final static String CONTENT_TYPE_MULTIPART = CONTENT_TYPE_MULTIPART_RELATED;
	public final static String CONTENT_TYPE_MULTIPART_PARAMETER_BOUNDARY = "boundary";
	public final static String CONTENT_TYPE_MULTIPART_PARAMETER_TYPE = "type";
	public final static String CONTENT_TYPE_MULTIPART_PARAMETER_START = "start";
	public final static String CONTENT_TYPE_MULTIPART_PARAMETER_START_INFO = "start-info";
	
	/** Source */
    protected final static String SEPARATOR_SOURCE = ":";
	
}

