package org.gov4j.commons.dao;



import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class Connection implements AutoCloseable {

	protected EntityManager entityManager = null;
	private EntityTransaction transaction = null;
	
	public Connection(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}
	
	public void beginTransaction() {
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
	} 
	
	public void commitTransaction() {
		if(this.transaction!=null) {
			this.transaction.commit();
		}
	} 
	
	public void rollbackTransaction() {
		if(this.transaction!=null) {
			this.transaction.rollback();
		}
	}
	
	@Override
	public void close() {
		this.entityManager.close();
	}
}
