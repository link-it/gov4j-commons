package org.gov4j.commons.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class DBManager {

	private JPAManager jpaManager;
	private EntityManagerFactory entityManagerFactory;
		
	public DBManager(JPAManager jpaManager) {
		this.jpaManager = jpaManager;
		this.entityManagerFactory = this.jpaManager.getEntityManagerFactory();
	}
	
	
	public Connection createConnection() {
		EntityManager entityManager = this.entityManagerFactory.createEntityManager();
		return new Connection(entityManager);
	}
	
	public void close(Connection transaction) {
		transaction.entityManager.close();
	}
	
	public <R> R run(ITransformerDB<R> code) throws Exception {
		try (Connection dbConn = this.createConnection()) {
			try {
				return code.apply(dbConn);
			} catch (Throwable e) {
				throw e;
			} 
		}
	}
	
	public void runVoid(IConsumerDB code) throws Exception {
	
		try (Connection dbConn = this.createConnection()) {
			try {
				code.accept(dbConn);
			} catch (Throwable e) {
				throw e;
			}
		}
	}
	
	public <R> R runTransaction(ITransformerDB<R> code) throws Exception {
		try (Connection dbConn = this.createConnection()) {
			boolean errorHappened = false;
	
			try {
				dbConn.beginTransaction();
				return code.apply(dbConn);
			} catch (Throwable e) {
				errorHappened = true;
				throw e;
			} finally {
				if (errorHappened) dbConn.rollbackTransaction();
				else dbConn.commitTransaction();
			}
		}
	}
	
	public void runTransactionVoid(IConsumerDB code) throws Exception {
	
		try (Connection dbConn = this.createConnection()) {
			boolean errorHappened = false;
	
			try {
				dbConn.beginTransaction();
				code.accept(dbConn);
			} catch (Throwable e) {
				errorHappened = true;
				throw e;
			} finally {
				if (errorHappened) dbConn.rollbackTransaction();
				else dbConn.commitTransaction();
			}
		}
	}
	
	@FunctionalInterface
	public interface ITransformerDB<R> {
		R apply(Connection dbConn) throws Exception;
	}
	
}
