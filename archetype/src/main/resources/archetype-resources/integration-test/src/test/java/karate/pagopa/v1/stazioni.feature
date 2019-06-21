Feature: Configurazione di una stazione intermediaria    

Background:

* def urlConfigurazione = urlGovPlugConfigurazione + '/v1/'

* def stazione = 
"""
{
	id: '99999999999_01',
	idIntermediario: '99999999999',
	password: 'p4ssw0rd'
}
"""

Given path urlConfigurazione
And path 'stazioni', stazione.id
When method delete

Scenario: Inserimento, lettura e rimozione di una stazione

Given url urlConfigurazione
And path 'stazioni'
And request stazione
When method post
Then status 201

Given path urlConfigurazione
And path 'stazioni', stazione.id
When method get
Then status 200
And match response == stazione

Given path urlConfigurazione
And path 'stazioni', stazione.id
When method delete
Then status 200