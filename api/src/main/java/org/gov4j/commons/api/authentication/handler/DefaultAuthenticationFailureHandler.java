package org.gov4j.commons.api.authentication.handler;

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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

	protected abstract CodiceEccezione<?> getEccezioneAutorizzazione();

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
			ByteArrayInputStream bais = null;
			ServletOutputStream outputStream = null;
			try{
				Response response = this.getEccezioneAutorizzazione().toFaultResponse(exception);
				res.setStatus(response.getStatus());

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
							res.setHeader(headerKey, sb.toString());
						}
					}
				}

				ObjectMapper mapper = JacksonJsonProvider.getObjectMapper();
				String fault = mapper.writeValueAsString(response.getEntity());
				bais = new ByteArrayInputStream(fault.getBytes());

				outputStream = res.getOutputStream();

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
//			
//			
//			
//			String msg;
//			if (exception instanceof BadCredentialsException) {
//				msg = "Credenziali errate";
//			} else if (exception instanceof DisabledException) {
//				msg = "Account non abilitato";
//			} else if (exception instanceof AccountExpiredException) {
//				msg = "L'account utilizzato è scaduto";
//			} else {
//				msg = "Errore durante l'autenticazione";
//			}
//
////			final String remote = userServices.getRemote(request);
////			String logMsg = msg + ", richiesta invitata dall'IP {}";
////			log.error(logMsg, remote);
//
//			// imposta gli header della risposta
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//			// invia la risposta al client
//			String jsonMsg = "{\"message\":\"" + msg + "\"}";
//			response.getWriter().print(jsonMsg);
//			response.getWriter().flush();
		}

}
