package org.gov4j.commons.api.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gov4j.commons.api.exception.CodiceEccezione;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public abstract class AbstractBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	protected abstract void fillResponse(AuthenticationException authException, javax.servlet.http.HttpServletResponse httpResponse);
	protected abstract void addCustomHeaders(javax.servlet.http.HttpServletResponse httpResponse);
	protected abstract CodiceEccezione<?> getEccezioneAutorizzazione();

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        this.addCustomHeaders(response);
    	this.fillResponse(authException, response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

}