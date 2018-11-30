package org.gov4j.commons.dao;

import org.gov4j.commons.core.logger.AbstractLogger;

public abstract class AbstractDAO extends AbstractLogger {

	private String idOperazione;
	private String prefix;
	private Connection dbConnection;	
	
	public AbstractDAO(String idOperazione, Connection dbConnection) {
		super(org.slf4j.LoggerFactory.getLogger(AbstractDAO.class));
		this.idOperazione = idOperazione;
		this.dbConnection = dbConnection;
		this.prefix = String.format("[%s] ", this.idOperazione);
	}
	
	
	@Override
	protected String appendPrefix(String message) {
		StringBuilder bf = new StringBuilder(this.prefix);
		bf.append(message);
		return bf.toString();
	}
	
	public Connection getConnection() {
		return this.dbConnection;
	}

}
