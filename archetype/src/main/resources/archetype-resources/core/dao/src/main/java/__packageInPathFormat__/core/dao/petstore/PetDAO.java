#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.dao.petstore;

import org.gov4j.commons.dao.AbstractCRUDDAO;
import org.gov4j.commons.dao.Connection;
import ${package}.core.dao.petstore.filters.PetFilter;
import ${package}.core.orm.vo.petstore.PetVO;

public class PetDAO extends AbstractCRUDDAO<PetVO, PetFilter>{

	public PetDAO(String idOperazione, Connection dbConn) {
		super(idOperazione, "PetVO", dbConn);
	}
}
