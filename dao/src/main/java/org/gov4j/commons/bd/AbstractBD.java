package org.gov4j.commons.bd;

import org.gov4j.commons.core.logger.AbstractLogger;
import org.gov4j.commons.dao.DBManager;

public class AbstractBD extends AbstractLogger{

	protected String idOperazione;
	protected String prefix;
	protected DBManager dbManager;
	

	public AbstractBD(String idOperazione, DBManager dbManager) {
		super(org.slf4j.LoggerFactory.getLogger(AbstractBD.class));
		this.idOperazione = idOperazione;
		this.dbManager = dbManager;
		this.prefix = String.format("[%s] ", this.idOperazione);
	}
	
	public String getIdOperazione() {
		return this.idOperazione;
	}
	
	public DBManager getDbManager() {
		return this.dbManager;
	}
	
	@Override
	protected String appendPrefix(String message) {
		StringBuilder bf = new StringBuilder(this.prefix);
		bf.append(message);
		return bf.toString();
	}
}
