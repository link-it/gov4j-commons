package org.gov4j.commons.dao.filters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gov4j.commons.core.exception.ServiceException;

public class WriterUtilities {

	private WriterUtilities() {}
	
	public static <T> Query getUpdateQuery(Class<T> c, AbstractFilter<T> filter, EntityManager em, Map<String, Object> setFields) throws ServiceException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<T> criteriaUpdate = cb.createCriteriaUpdate(c);
		Root<T> root = criteriaUpdate.from(c);
		
		Iterator<String> itKeys = setFields.keySet().iterator();
		while (itKeys.hasNext()) {
			String attributeName = (String) itKeys.next();
			Object value = setFields.get(attributeName);
			criteriaUpdate.set(attributeName, value);
		}
		
		where(cb,criteriaUpdate,filter.toWhere(cb, root));
		
		return em.createQuery(criteriaUpdate);
		
    }
	
	public static <T> Query getDeleteQuery(Class<T> c, AbstractFilter<T> filter, EntityManager em) throws ServiceException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(c);
		Root<T> root = criteriaDelete.from(c);
		
		where(cb,criteriaDelete,filter.toWhere(cb, root));
		
		return em.createQuery(criteriaDelete);
		
    }
	
	public static <T>  void where(CriteriaBuilder cb, CriteriaUpdate<?> criteriaUpdate,List<Predicate> list) {
		if(list!=null && !list.isEmpty()) {
			CriteriaUpdate<?> where = criteriaUpdate.where(cb.and(list.toArray(new Predicate[]{})));
			Predicate p =where.getRestriction();
			criteriaUpdate.where(p);
		}
	}
	
	public static <T>  void where(CriteriaBuilder cb, CriteriaDelete<?> criteriaDelete,List<Predicate> list) {
		if(list!=null && !list.isEmpty()) {
			CriteriaDelete<?> where = criteriaDelete.where(cb.and(list.toArray(new Predicate[]{})));
			Predicate p =where.getRestriction();
			criteriaDelete.where(p);
		}
	}
	
}
