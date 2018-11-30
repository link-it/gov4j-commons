package org.gov4j.commons.dao.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.FilterSortWrapper;

public abstract class AbstractFilterJoin<T> extends AbstractFilter<T> {

	protected AbstractFilterJoin(Class<T> c) {
		super(c);
	}

	public List<Order> orderBy(CriteriaBuilder cb, Root<T> root, Map<String,Join<T, ?>> mapJoin) throws ServiceException {

		List<Order> orderBy = new ArrayList<>();
		if(this.filterSortList!=null && !this.filterSortList.isEmpty()) {
			for(FilterSortWrapper filter: this.filterSortList) {
				String field = filter.getField();
				Path<T> filterPath = null;
				if(filter.getJoinTable()!=null) {
					if(mapJoin.containsKey(filter.getJoinTable())) {
						filterPath = mapJoin.get(filter.getJoinTable()).get(field);
					}
					else {
						throw new ServiceException("JoinTable '"+filter.getJoinTable()+"' undefined in map");
					}
				}
				else {
					filterPath = root.get(field);
				}
				Order order = filter.getSortOrderAsc()? cb.asc(filterPath) : cb.desc(filterPath); 
				orderBy.add(order);
			}
			
		} else {
			Path<T> filterPath = root.get("id");
			if(filterPath!=null) {
				Order order = cb.desc(filterPath); 
				orderBy.add(order);
			}
		}	

		return orderBy;
	}
	
	@Override
	public TypedQuery<T> getQuery(EntityManager em) throws ServiceException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<T> criteriaQuery = cb.createQuery(this.c);
    	Root<T> root = criteriaQuery.from(this.c);
    	criteriaQuery.select(root);
    	Map<String,Join<T, ?>> mapJoin = this.join(cb, root);
    	if(mapJoin!=null) {
    		where(cb,criteriaQuery,this.toWhere(cb, root, mapJoin));
        	criteriaQuery.orderBy(this.orderBy(cb, root, mapJoin));
        }
        else {
        	where(cb,criteriaQuery,this.toWhere(cb, root));
        	criteriaQuery.orderBy(this.orderBy(cb, root));
        }
        return this.generateQuery(em, criteriaQuery);
    }
	
	@Override
	public TypedQuery<Long> getCountQuery(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
    	Root<T> root = criteriaQuery.from(this.c);
        criteriaQuery.select(cb.count(root));
        Map<String,Join<T, ?>> mapJoin = this.join(cb, root);
        if(mapJoin!=null) {
        	where(cb,criteriaQuery,this.toWhere(cb, root, mapJoin));
        }
        else {
        	where(cb,criteriaQuery,this.toWhere(cb, root));
        }
        return em.createQuery(criteriaQuery);
    }
	
	@Override
	protected List<Predicate> toWhere(CriteriaBuilder criteriaBuilder, Root<T> root) {
		return this.toWhere(criteriaBuilder, root, null);
	}
	
	protected abstract List<Predicate> toWhere(CriteriaBuilder criteriaBuilder, Root<T> root, Map<String,Join<T, ?>> mapJoin);
	
	protected abstract Map<String,Join<T, ?>> join(CriteriaBuilder criteriaBuilder, Root<T> root);
}
