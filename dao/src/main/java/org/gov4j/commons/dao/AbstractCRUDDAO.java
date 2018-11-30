package org.gov4j.commons.dao;

import java.util.Map;

import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.filters.AbstractWriter;

public abstract class AbstractCRUDDAO<O, F extends AbstractWriter<O>> 
	extends AbstractSearchDAO<O, F> 
	implements ICRUDDAO<O, F> {

	public AbstractCRUDDAO(String idOperazione, String objectName, Connection dbConnection) {
		super(idOperazione, objectName, dbConnection);
	}
	
	@Override
	public void create(O object) throws ServiceException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Creazione '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
			
			BasicWriteDAO<O, F> writeDAO = new BasicWriteDAO<>(this.getConnection().getEntityManager());
			
			writeDAO.insert(object);
			
			sb = new StringBuilder();
			sb.append("Creazione '").append(this.objectName).append("' completata");
			this.info(sb.toString());
			
			return;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Creazione '").append(this.objectName).append("' fallita: ").append(e.getMessage());
			this.error(sb.toString(), e);
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void update(O object) throws ServiceException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Aggiornamento '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
		
			BasicWriteDAO<O, F> writeDAO = new BasicWriteDAO<>(this.getConnection().getEntityManager());
			
			writeDAO.update(object);
			
			sb = new StringBuilder();
			sb.append("Aggiornamento '").append(this.objectName).append("' completato");
			this.info(sb.toString());
			
			return;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Aggiornamento '").append(this.objectName).append("' fallito: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
	
	@Override
	public void updateFields(Map<String, Object> setFields, F filter) throws ServiceException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Aggiornamento colonne '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
			
			BasicWriteDAO<O, F> writeDAO = new BasicWriteDAO<>(this.getConnection().getEntityManager());
			
			writeDAO.update(setFields, filter);
			
			sb = new StringBuilder();
			sb.append("Aggiornamento colonne '").append(this.objectName).append("' completato");
			this.info(sb.toString());
			
			return;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Aggiornamento colonne '").append(this.objectName).append("' fallito: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void delete(O object) throws ServiceException {
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
		
			BasicWriteDAO<O, F> writeDAO = new BasicWriteDAO<>(this.getConnection().getEntityManager());
			
			writeDAO.delete(object);
			
			sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' completato");
			this.info(sb.toString());
			
			return;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' fallito: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void deleteAll(F filter) throws ServiceException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' in corso...");
			this.info(sb.toString());
			
			BasicWriteDAO<O, F> writeDAO = new BasicWriteDAO<>(this.getConnection().getEntityManager());
			
			writeDAO.delete(filter);
			
			sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' completato");
			this.info(sb.toString());
			
			return;
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Eliminazione '").append(this.objectName).append("' fallito: ").append(e.getMessage());
			this.error(sb.toString(), e);
			
			throw new ServiceException(e);
		}
	}
	
}
