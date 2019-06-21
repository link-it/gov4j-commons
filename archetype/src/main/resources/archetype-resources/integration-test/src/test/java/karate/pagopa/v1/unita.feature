Feature: Configurazione di un dominio intermediato    

Background:

* def urlConfigurazione = urlGovPlugConfigurazione + '/v1/'
* def dominio = 
"""
{
	id: '01234567890',
  denominazione: 'Ente Creditore Test',
  indirizzo: 'Piazza della Vittoria',
  civico: '1',
  cap: '00000',
  localita: 'Roma',
  provincia: 'RO',
  nazione: 'IT'
}
"""

Given url urlConfigurazione, 
And path 'domini'
And request dominio
When method post
Then assert responseStatus == 200 || responseStatus == 201

* def unita = 
"""
{
	id: 'UFF-1',
  denominazione: 'Ufficio Brevetti',
  indirizzo: 'Largo Augusto',
  civico: '2',
  cap: '11111',
  localita: 'Milano',
  provincia: 'MI',
  nazione: 'IT'
}
"""

Given path urlConfigurazione
And path 'domini', dominio.id, 'unita', unita.id
When method delete

Scenario: Inserimento, lettura e cancellazione di una unita operativa
					
Given url urlConfigurazione, 
And path 'domini', dominio.id, 'unita'
And request unita
When method post
Then status 201

Given path urlConfigurazione
And path 'domini', dominio.id, 'unita', unita.id
When method get
Then status 200
And match response == unita

Given path urlConfigurazione
And path 'domini', dominio.id, 'unita', unita.id
When method delete
Then status 200

