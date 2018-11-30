package org.gov4j.commons.dao.filters;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.FilterSortWrapper;

public abstract class AbstractFilter<T> {

	protected Long id;
	protected Integer offset;
	protected Integer limit;
	protected List<FilterSortWrapper> filterSortList;
	protected Class<T> c;
	
	protected AbstractFilter(Class<T> c) {
		this.c = c;
	}

	public List<Order> orderBy(CriteriaBuilder cb, Root<T> root) {

		List<Order> orderBy = new ArrayList<>();

		if(this.filterSortList!=null && !this.filterSortList.isEmpty()) {
			for(FilterSortWrapper filter: this.filterSortList) {
				String field = filter.getField();
				Path<?> filterPath = root.get(field);
				Order order = filter.getSortOrderAsc()? cb.asc(filterPath) : cb.desc(filterPath); 
				orderBy.add(order);
			}
			
		} else {
			Order order = cb.desc(root.get("id")); 
			orderBy.add(order);
		}

		return orderBy;
	}
	
	public TypedQuery<T> getQuery(EntityManager em) throws ServiceException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<T> criteriaQuery = cb.createQuery(this.c);
    	Root<T> root = criteriaQuery.from(this.c);
    	criteriaQuery.select(root);
    	where(cb,criteriaQuery,this.toWhere(cb, root));
        criteriaQuery.orderBy(this.orderBy(cb, root));
        return this.generateQuery(em, criteriaQuery);
    }
	
	protected TypedQuery<T> generateQuery(EntityManager em, CriteriaQuery<T> criteriaQuery){
        TypedQuery<T> query = em.createQuery(criteriaQuery);
        if(this.offset!=null) {
        	query.setFirstResult(this.offset);
        }
        if(this.limit!=null) {
        	query.setMaxResults(this.limit);
        }
        return query;
	}
	
	public TypedQuery<Long> getCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
    	Root<T> root = criteriaQuery.from(this.c);
        criteriaQuery.select(cb.count(root));
        where(cb,criteriaQuery,this.toWhere(cb, root));
        return em.createQuery(criteriaQuery);
    }

	protected void where(CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery,List<Predicate> list) {
		if(list!=null && !list.isEmpty()) {
			CriteriaQuery<?> where = criteriaQuery.where(cb.and(list.toArray(new Predicate[]{})));
			Predicate p =where.getRestriction();
			criteriaQuery.where(p);
		}
	}
	
	protected abstract List<Predicate> toWhere(CriteriaBuilder criteriaBuilder, Root<T> root);
		
	public void setFilterSortList(List<FilterSortWrapper> filterSortList) {
		this.filterSortList = filterSortList;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
