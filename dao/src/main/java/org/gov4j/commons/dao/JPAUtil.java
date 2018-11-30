package org.gov4j.commons.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.gov4j.commons.dao.constants.DatabaseType;
import org.hibernate.cfg.AvailableSettings;

public class JPAUtil {

	private String persistenceUnit;

	private JPAUtil(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}
	
	public static JPAUtil getInstance(String persistenceUnit) {
		return new JPAUtil(persistenceUnit);
	}

	public EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(this.persistenceUnit);  
	}
	public EntityManagerFactory createEntityManagerFactory(DatabaseType tipoDB) {
		return this.createEntityManagerFactory(JPAUtil.convertDialect(tipoDB));
	}
	public EntityManagerFactory createEntityManagerFactory(String dialect) {
		Map<String, Object> props = new HashMap<>();
		props.put(org.hibernate.cfg.AvailableSettings.DIALECT, dialect);
		return Persistence.createEntityManagerFactory(this.persistenceUnit);  
	}
	
	public EntityManagerFactory createEntityManagerFactory(DatabaseType tipoDB, String connectionUrl, String driverJDBC, String username, String password) {
		return this.createEntityManagerFactory(JPAUtil.convertDialect(tipoDB), connectionUrl, driverJDBC, username, password);
	}
	public EntityManagerFactory createEntityManagerFactory(String dialect, String connectionUrl, String driverJDBC, String username, String password) {
		Map<String, Object> props = new HashMap<>();
		if(dialect!=null) {
			props.put(org.hibernate.cfg.AvailableSettings.DIALECT, dialect);
		}
		props.put(AvailableSettings.JPA_JDBC_URL , connectionUrl);
		props.put(AvailableSettings.JPA_JDBC_DRIVER , driverJDBC);
		if(username!=null) {
			props.put(AvailableSettings.JPA_JDBC_USER , username);
		}
		if(password!=null) {
			props.put(AvailableSettings.JPA_JDBC_PASSWORD , password);
		}
		return Persistence.createEntityManagerFactory(this.persistenceUnit, props);  
	}
	
	public EntityManagerFactory createEntityManagerFactory(DatabaseType tipoDB, String jndiNameDS, Properties ctx) throws NamingException {
		return this.createEntityManagerFactory(JPAUtil.convertDialect(tipoDB), jndiNameDS, ctx);
	}
	public EntityManagerFactory createEntityManagerFactory(String dialect, String jndiNameDS, Properties ctx) throws NamingException {
		if(ctx==null) {
			ctx = new Properties();
		}
		InitialContext search = null;
		DataSource ds = null;
		try{
			search = new InitialContext(ctx);
			ds = (DataSource) search.lookup(jndiNameDS);
		}finally{
			if(search!=null) {
				search.close();
			}
		}
		return this.createEntityManagerFactory(dialect, ds);
	}
	public EntityManagerFactory createEntityManagerFactory(DatabaseType tipoDB, DataSource ds) {
		return this.createEntityManagerFactory(JPAUtil.convertDialect(tipoDB), ds);
	}
	public EntityManagerFactory createEntityManagerFactory(String dialect, DataSource ds) {
		Map<String, Object> props = new HashMap<>();
		if(dialect!=null) {
			props.put(org.hibernate.cfg.AvailableSettings.DIALECT, dialect);
		}
		props.put(org.hibernate.cfg.AvailableSettings.JPA_NON_JTA_DATASOURCE, ds);
		return Persistence.createEntityManagerFactory(this.persistenceUnit, props);  
	}
	
	private static String convertDialect(DatabaseType DatabaseType) {
		switch (DatabaseType) {
		case POSTGRESQL:
			return org.hibernate.dialect.PostgreSQL9Dialect.class.getName();
		case MYSQL:
			return org.hibernate.dialect.MySQL8Dialect.class.getName();
		case ORACLE:
			return org.hibernate.dialect.Oracle10gDialect.class.getName();
		case DERBY:
			return org.hibernate.dialect.DerbyTenFiveDialect.class.getName();
		}
		return null;
	}
}
