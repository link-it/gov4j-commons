package org.gov4j.commons.dao;

import java.util.Map;

import javax.persistence.EntityManager;

import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.filters.AbstractWriter;

public class BasicWriteDAO<E, F extends AbstractWriter<E>> {

protected EntityManager em;
	
	public BasicWriteDAO(EntityManager em) {
		this.em = em;
	}
	
	public void insert(E entity) {
		this.em.persist(entity);
		this.em.flush();
	}
	
	public void update(E entity) {
		this.em.merge(entity);
		this.em.flush();
	}
	
	public void update(Map<String, Object> setFields, F updateCriteria) throws ServiceException {
		updateCriteria.getUpdateQuery(this.em, setFields).executeUpdate();
		this.em.flush();
	}
	
	public void delete(E entity) {
		this.em.remove(entity);
		this.em.flush();
	}
	
	public void delete(F updateCriteria) throws ServiceException {
		updateCriteria.getDeleteQuery(this.em).executeUpdate();
		this.em.flush();
	}
	
}
