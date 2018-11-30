package org.gov4j.commons.dao;

import java.util.List;

import org.gov4j.commons.core.exception.MultipleResultException;
import org.gov4j.commons.core.exception.NotFoundException;
import org.gov4j.commons.core.exception.NotImplementedException;
import org.gov4j.commons.core.exception.ServiceException;

// O=Oggetto, F=Filter
public interface ISearchDAO<O,F> {

	public O get(long id) throws ServiceException,NotFoundException,NotImplementedException;
		
	public List<O> findAll(F filter) throws ServiceException,NotImplementedException;
	
	public O find(F filter) throws ServiceException,NotFoundException,MultipleResultException,NotImplementedException;
	
	public long count(F filter) throws ServiceException,NotImplementedException;
	
}
