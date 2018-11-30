package org.gov4j.commons.api.authentication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.gov4j.commons.api.converter.jackson.JacksonJsonProvider;
import org.gov4j.commons.api.exception.CodiceEccezione;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class FormAuthenticationEntryPoint implements AuthenticationEntryPoint {

	protected abstract CodiceEccezione<?> getEccezioneAutorizzazione();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
		ByteArrayInputStream bais = null;
		ServletOutputStream outputStream = null;
		try{
			Response response = this.getEccezioneAutorizzazione().toFaultResponse(authException);
			httpResponse.setStatus(response.getStatus());

			MultivaluedMap<String, Object> headers = response.getHeaders();
			if(!headers.isEmpty()) {
				Set<String> keySet = headers.keySet();

				for (String headerKey : keySet) {
					List<Object> list = headers.get(headerKey);
					if(!list.isEmpty()) {
						StringBuilder sb = new StringBuilder();
						for (Object object : list) {
							if(sb.length() > 0)
								sb.append(", ");

							sb.append(object);
						}
						httpResponse.setHeader(headerKey, sb.toString());
					}
				}
			}

			ObjectMapper mapper = JacksonJsonProvider.getObjectMapper();
			String fault = mapper.writeValueAsString(response.getEntity());
			bais = new ByteArrayInputStream(fault.getBytes());

			outputStream = httpResponse.getOutputStream();

			IOUtils.copy(bais, outputStream);

			outputStream.flush();
		}catch(Exception e) {

		} finally {
			if(bais!= null) {
				try {
					bais.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
