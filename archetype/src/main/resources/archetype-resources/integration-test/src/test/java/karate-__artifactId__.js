function fn() {    
	var env = karate.env; // get system property 'karate.env'
	if (!env) {
		env = 'dev';
	}

	karate.configure('connectTimeout', 20000);
	karate.configure('readTimeout', 20000);  
	
	var config = {
		env: env,
		urlGovPlugConfigurazione: 'http://localhost:8080/config/api/config',
	}
	if (env == 'dev') {
		// customize
		// e.g. config.foo = 'bar';
	} else if (env == 'e2e') {
		// customize
	}
	return config;
}