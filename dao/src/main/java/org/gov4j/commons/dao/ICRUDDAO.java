package org.gov4j.commons.dao;

import java.util.Map;

import org.gov4j.commons.core.exception.NotImplementedException;
import org.gov4j.commons.core.exception.ServiceException;

// O=Oggetto, F=Filter
public interface ICRUDDAO<O,F> extends ISearchDAO<O, F> {

	public void create(O object) throws ServiceException,NotImplementedException;
	
	public void update(O object) throws ServiceException,NotImplementedException;
	
	public void updateFields(Map<String, Object> setFields, F filter) throws ServiceException,NotImplementedException;
	
	public void delete(O object) throws ServiceException,NotImplementedException;
	
	public void deleteAll(F filter) throws ServiceException,NotImplementedException;
	
}
