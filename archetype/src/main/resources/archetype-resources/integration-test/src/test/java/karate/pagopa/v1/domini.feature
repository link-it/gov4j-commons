Feature: Configurazione di un dominio intermediato    

Background:

* def urlConfigurazione = urlGovPlugConfigurazione + '/v1/'

* def dominio = 
"""
{
	id: '01234567890',
  anagrafica: 'Ente Creditore Test',
  indirizzo: 'Piazza della Vittoria',
  civico: '1',
  cap: '00000',
  localita: 'Roma',
  provincia: 'RO',
  nazione: 'IT'
}
"""

Scenario: Inserimento, lettura e rimozione di un dominio

Given url urlConfigurazione
And path 'domini'
And request dominio, dominio.id
When method delete

Given url urlConfigurazione 
And path 'domini'
And request dominio
When method post
Then status 204

Given url urlConfigurazione
And path 'domini', dominio.id
When method get
Then status 200
And match response == dominio

Given url urlConfigurazione, 
And path 'domini'
And request dominio, dominio.id
When method delete
Then status 200

Scenario Outline: Aggiornamento di un dominio

Given url urlConfigurazione
And path 'domini'
And request dominio
When method post
Then match responseStatus == 200 || responseStatus == 201

* set dominio.<field> = <value>

Given url urlConfigurazione, 
And path 'domini'
And request dominio
When method post
Then status 200

Given path urlConfigurazione
And path 'domini', dominio.id
When method get
Then status 200
And match response == dominio

Examples:
| field | value |
| id | 00000000000 |
| denominazione | 'test-modifica' |
| indirizzo | 'test-modifica' |
| civico | 'test-modifica' |
| cap | 'test-modifica' |
| localita | 'test-modifica' |
| provincia | 'test-modifica' |
| nazione | 'UK' |

