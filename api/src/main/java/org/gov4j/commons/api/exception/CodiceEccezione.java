package org.gov4j.commons.api.exception;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public abstract class CodiceEccezione<T> {

	private final String name;
	private final String descrizione;
	private final int code;

	public CodiceEccezione(int code, String name, String descrizione) {
		this.code = code;
		this.name = name;
		this.descrizione = descrizione;
	}

	public int getCode() {
		return this.code;
	}
	public String getName() {
		return this.name;
	}
	public String getDescrizione()
	{
		return this.descrizione;
	}

	@Override
	public String toString() {
		return this.descrizione;
	}

	public abstract T toFaultBean();
	
	public abstract T toFaultBean(String dettaglio);
	public abstract T toFaultBean(Throwable e);

	public ResponseBuilder toFaultResponseBuilder() {
		return this.toFaultResponseBuilder(true);
	}
	public ResponseBuilder toFaultResponseBuilder(boolean addFaultBean) {
		T faultBean = this.toFaultBean();
		ResponseBuilder rb = Response.status(this.code);
		if(addFaultBean) {
			rb.entity(faultBean).type(MediaType.APPLICATION_JSON);
		}
		return rb;
	}
	public ResponseBuilder toFaultResponseBuilder(String dettaglio) {
		T faultBean = this.toFaultBean(dettaglio);
		return Response.status(this.code).entity(faultBean).type(MediaType.APPLICATION_JSON);
	}
	public ResponseBuilder toFaultResponseBuilder(Throwable e) {
		T faultBean = this.toFaultBean(e);
		return Response.status(this.code).entity(faultBean).type(MediaType.APPLICATION_JSON);
	}
	
	public Response toFaultResponse() {
		return this.toFaultResponse(true);
	}
	public Response toFaultResponse(boolean addFaultBean) {
		return this.toFaultResponseBuilder(addFaultBean).build();
	}
	public Response toFaultResponse(String dettaglio) {
		return this.toFaultResponseBuilder(dettaglio).build();
	}
	public Response toFaultResponse(Throwable e) {
		return this.toFaultResponseBuilder(e).build();
	}

	public javax.ws.rs.WebApplicationException toException(ResponseBuilder responseBuilder){
		return new javax.ws.rs.WebApplicationException(responseBuilder.build());
	}
	public javax.ws.rs.WebApplicationException toException(ResponseBuilder responseBuilder, Map<String, String> headers){
		if(headers!=null && !headers.isEmpty()) {
			headers.keySet().stream().forEach(k -> {
				responseBuilder.header(k, headers.get(k));
			});
		}
		return new javax.ws.rs.WebApplicationException(responseBuilder.build());
	}
	public javax.ws.rs.WebApplicationException toException(Response response){
		// Aggiunta eccezione nel costruttore, in modo che cxf chiami la classe WebApplicationExceptionMapper
		return new javax.ws.rs.WebApplicationException(new Exception(response.getEntity().toString()),response);
	}
	public javax.ws.rs.WebApplicationException toException(){
		return this.toException(true);
	}
	public javax.ws.rs.WebApplicationException toException(boolean addFaultBean){
		Response r = this.toFaultResponse(addFaultBean);
		return this.toException(r);
	}
	public javax.ws.rs.WebApplicationException toException(String dettaglio){
		Response r = this.toFaultResponse(dettaglio);
		return this.toException(r);
	}
	public javax.ws.rs.WebApplicationException toException(Throwable e){
		Response r = this.toFaultResponse(e);
		return this.toException(r);
	}

	public void throwException(ResponseBuilder responseBuilder) throws javax.ws.rs.WebApplicationException{
		throw this.toException(responseBuilder);
	}
	public void throwException(Response response) throws javax.ws.rs.WebApplicationException{
		throw this.toException(response);
	}
	public void throwException() throws javax.ws.rs.WebApplicationException{
		throw this.toException();
	}
	public void throwException(boolean addFaultBean) throws javax.ws.rs.WebApplicationException{
		throw toException(addFaultBean);
	}
	public void throwException(String dettaglio) throws javax.ws.rs.WebApplicationException{
		throw toException(dettaglio);
	}
	public void throwException(Throwable e) throws javax.ws.rs.WebApplicationException{
		throw toException(e);
	}
	

}
