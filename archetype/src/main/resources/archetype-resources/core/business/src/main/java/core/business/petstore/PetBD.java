#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.business.petstore;

import java.util.List;

import org.gov4j.commons.bd.AbstractBD;
import org.gov4j.commons.core.exception.ServiceException;
import org.gov4j.commons.dao.DBManager;
import ${package}.core.dao.petstore.PetDAO;
import ${package}.core.dao.petstore.filters.PetFilter;
import ${package}.core.orm.vo.petstore.PetVO;


public class PetBD extends AbstractBD {

	public PetBD(String idOperazione, DBManager dbManager) {
		super(idOperazione, dbManager);
	}
	
	public List<PetVO> findAll() throws ServiceException {
		
		try {			
			return this.getDbManager().runTransaction( dbConn ->
				new PetDAO(this.idOperazione, dbConn).findAll(new PetFilter())
			);
		} catch (Exception e) { 
			throw new ServiceException("Errore durante il recupero dei Pet.", e);
		}
	}

	public void create(PetVO pet) throws ServiceException {
		
		try {			
			this.getDbManager().runTransactionVoid( dbConn ->
				new PetDAO(this.idOperazione, dbConn).create(pet)
			);
		} catch (Exception e) { 
			throw new ServiceException("Errore durante la creazione di un Pet.", e);
		}
	}

	
}
