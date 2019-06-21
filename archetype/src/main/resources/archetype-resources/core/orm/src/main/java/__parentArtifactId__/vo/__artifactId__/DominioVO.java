#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.vo.${artifactId};

public class DominioVO {

	private Long id;
	
	private String identificativoDominio;
	private String anagrafica;
	private String indirizzo;
	private String civico;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificativoDominio() {
		return this.identificativoDominio;
	}
	public void setIdentificativoDominio(String identificativoDominio) {
		this.identificativoDominio = identificativoDominio;
	}
	public String getAnagrafica() {
		return this.anagrafica;
	}
	public void setAnagrafica(String anagrafica) {
		this.anagrafica = anagrafica;
	}
	public String getIndirizzo() {
		return this.indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return this.civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCap() {
		return this.cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getLocalita() {
		return this.localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return this.provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return this.nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

}
