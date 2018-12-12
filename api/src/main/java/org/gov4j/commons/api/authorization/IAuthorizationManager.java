package org.gov4j.commons.api.authorization;

import org.gov4j.commons.api.impl.ServiceContext;

public interface IAuthorizationManager {

	public void authorize(ServiceContext context);

}
