package org.gov4j.commons.dao.filters;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;

import org.gov4j.commons.core.exception.ServiceException;

public abstract class AbstractWriterJoin<T> extends AbstractFilterJoin<T> {

	protected AbstractWriterJoin(Class<T> c) {
		super(c);
	}
	
	public Query getUpdateQuery(EntityManager em, Map<String, Object> setFields) throws ServiceException {
		return WriterUtilities.getUpdateQuery(this.c, this, em, setFields);		
    }
	
	public Query getDeleteQuery(EntityManager em) throws ServiceException {
		return WriterUtilities.getDeleteQuery(this.c, this, em);
    }
	
	protected void where(CriteriaBuilder cb, CriteriaUpdate<?> criteriaUpdate,List<Predicate> list) {
		WriterUtilities.where(cb, criteriaUpdate, list);
	}
	
	protected void where(CriteriaBuilder cb, CriteriaDelete<?> criteriaDelete,List<Predicate> list) {
		WriterUtilities.where(cb, criteriaDelete, list);
	}
	
}
