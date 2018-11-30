package org.gov4j.commons.dao;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.gov4j.commons.dao.constants.DatabaseType;
import org.slf4j.Logger;

public class JPAManager {

	private final static Logger log = org.slf4j.LoggerFactory.getLogger(JPAManager.class);
	
	private JPAUtil jpaUtil;
	public JPAManager(String persistenceUnit) {
		this.jpaUtil = JPAUtil.getInstance(persistenceUnit);
	}
	
	private EntityManagerFactory entityManagerFactory;

	public synchronized void createEntityManagerFactory() {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory();
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public synchronized void createEntityManagerFactory(DatabaseType tipoDB) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(tipoDB);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public synchronized void createEntityManagerFactory(String dialect) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(dialect);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public synchronized void createEntityManagerFactory(DatabaseType tipoDB, String connectionUrl, String driverJDBC, String username, String password) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(tipoDB, connectionUrl, driverJDBC, username, password);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public synchronized void createEntityManagerFactory(String dialect, String connectionUrl, String driverJDBC, String username, String password) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(dialect, connectionUrl, driverJDBC, username, password);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public synchronized void createEntityManagerFactory(DatabaseType tipoDB, String jndiNameDS, Properties ctx) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(tipoDB, jndiNameDS, ctx);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public synchronized void createEntityManagerFactory(String dialect, String jndiNameDS, Properties ctx) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(dialect, jndiNameDS, ctx);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public synchronized void createEntityManagerFactory(DatabaseType tipoDB, DataSource ds) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(tipoDB, ds);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public synchronized void createEntityManagerFactory(String dialect, DataSource ds) {
		try {
			JPAManager.log.info("EntityManagerFactory init...");
			this.entityManagerFactory = this.jpaUtil.createEntityManagerFactory(dialect, ds);
			JPAManager.log.info("EntityManagerFactory init ok");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}
	
	public void closeEntityManagerFactory() {
		if(this.entityManagerFactory!=null) {
			this.entityManagerFactory.close();
			this.entityManagerFactory = null;
		}
	}

}