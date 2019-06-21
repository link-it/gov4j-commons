#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.govlet.pagopa.${parentArtifactId}.${artifactId}.vo.config;

public class StazioneVO {

	private Long id;
	
	private String identificativoStazione;
	private String intermediario;
	private String password;

	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdentificativoStazione() {
		return this.identificativoStazione;
	}
	public void setIdentificativoStazione(String identificativoStazione) {
		this.identificativoStazione = identificativoStazione;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIntermediario() {
		return intermediario;
	}
	public void setIntermediario(String intermediario) {
		this.intermediario = intermediario;
	}
}
