package org.gov4j.commons.api.exception;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;


public class WebApplicationExceptionMapper implements ExceptionMapper<javax.ws.rs.WebApplicationException> {

	private boolean excludeFaultBean;
	
	@Override
	public Response toResponse(javax.ws.rs.WebApplicationException e) {
//		if(e.getResponse()==null || e.getResponse().getEntity()==null || !(e.getResponse().getEntity() instanceof FaultBean)) {
//			return CodiceEccezione.RICHIESTA_NON_VALIDA.toFaultResponse(e);
//		}
//		else {
			if(this.excludeFaultBean) {
				ResponseBuilder responseBuilder = Response.status(e.getResponse().getStatus());
				if(e.getResponse().getHeaders()!=null) {
					MultivaluedMap<String, Object> map = e.getResponse().getHeaders();
					if(!map.isEmpty()) {
						map.keySet().stream().forEach(k -> {
							responseBuilder.header(k, map.get(k));
						});
					}
				}
				return responseBuilder.build();
			} else {
				return e.getResponse();
			}
//		}
	}

	public boolean isExcludeFaultBean() {
		return this.excludeFaultBean;
	}

	public void setExcludeFaultBean(boolean excludeFaultBean) {
		this.excludeFaultBean = excludeFaultBean;
	}
}
