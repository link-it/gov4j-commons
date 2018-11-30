package org.gov4j.commons.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gov4j.commons.core.exception.MultipleResultException;
import org.gov4j.commons.core.exception.NotFoundException;
import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.filters.AbstractFilter;

public abstract class AbstractSearchDAO<O, F extends AbstractFilter<O>> 
	extends AbstractDAO 
	implements ISearchDAO<O, F> {

	protected String objectName;
	
	public AbstractSearchDAO(String idOperazione, String objectName, Connection connection) {
		super(idOperazione, connection);
		this.objectName = objectName;
	}
	
	@SuppressWarnings("unchecked")
	protected Class<O> getClassName(){
		return ((Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	@Override
	public O get(long id) throws ServiceException,NotFoundException {
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Lettura '").append(this.objectName).append("' (id:").append(id).append(") in corso...");
			this.info(sb.toString());
			
			BasicSearchDAO<O, F> searchDAO = new BasicSearchDAO<>(this.getConnection().entityManager);
			
			O object = searchDAO.get(this.getClassName(), id);
			if(object==null) {
				throw new NotFoundException();
			}
	
			sb = new StringBuilder();
			sb.append("Lettura '").append(this.objectName).append("' (id:").append(id).append(") completata");
			this.info(sb.toString());
			
			return object;
			
		} catch (Exception e) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("Lettura '").append(this.objectName).append("' (id:").append(id).append(") fallita: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			if(e instanceof NotFoundException) {
				throw e;
			}
			else {
				throw new ServiceException(e);
			}
		}

	}
	
	
	@Override
	public List<O> findAll(F filter) throws ServiceException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Ricerca '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
			
			//entityManager.getTransaction().begin(); // la transazione serve anche in lettura se ci sono LOB negli oggetti trattati.

			BasicSearchDAO<O, F> searchDAO = new BasicSearchDAO<>(this.getConnection().getEntityManager());
			
			List<O> findAll = searchDAO.findAll(filter);
			
			//entityManager.getTransaction().commit();

			sb = new StringBuilder();
			sb.append("Ricerca '").append(this.objectName).append("' completata");
			this.info(sb.toString());
			
			return findAll;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Lettura '").append(this.objectName).append("' fallita: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
	@Override
	public O find(F filter) throws ServiceException,NotFoundException,MultipleResultException {
		List<O> l = this.findAll(filter);
		if(l==null || l.isEmpty()) {
			throw new NotFoundException();
		}
		else if(l.size()>1) {
			throw new MultipleResultException();
		}
		else {
			return l.get(0);
		}
	}
	
	@Override
	public long count(F filter) throws ServiceException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Conteggio '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
			
			//entityManager.getTransaction().begin(); // la transazione serve anche in lettura se ci sono LOB negli oggetti trattati.

			BasicSearchDAO<O, F> searchDAO = new BasicSearchDAO<>(this.getConnection().getEntityManager());
			
			long count = searchDAO.count(filter);
			
			//entityManager.getTransaction().commit();

			sb = new StringBuilder();
			sb.append("Conteggio '").append(this.objectName).append("' completata");
			this.info(sb.toString());
			
			return count;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Conteggio '").append(this.objectName).append("' fallito: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
}
