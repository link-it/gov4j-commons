package org.gov4j.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.filters.AbstractFilter;


public class BasicSearchDAO<E, F extends AbstractFilter<E>> {
	
	protected EntityManager em;
	
	public BasicSearchDAO(EntityManager em) {
		this.em = em;
	}
	
	public long count(F filter) {
		return filter.getCountQuery(this.em).getSingleResult();

	} 
	
	public List<E> findAll(F filter) throws ServiceException {		
		return filter.getQuery(this.em).getResultList();		
	}

	public E get(Class<E> c, long id) {				
		return this.em.find(c, id);
	}

}

