#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.servlets.core.api.exception;

public class CodiceEccezione extends org.gov4j.commons.api.exception.CodiceEccezione<Object> {

	public static final CodiceEccezione ERRORE_INTERNO = new CodiceEccezione(500,"ERRORE INTERNO","Errore interno");

	public CodiceEccezione(int codice, String nome, String descrizione) {
		super(codice, nome, descrizione);
	}
	@Override
	public Object toFaultBean() {
		return new Object();
	}
	@Override
	public Object toFaultBean(String dettaglio) {
		return new Object();
	}
	@Override
	public Object toFaultBean(Exception e) {
		return new Object();
	}
}
