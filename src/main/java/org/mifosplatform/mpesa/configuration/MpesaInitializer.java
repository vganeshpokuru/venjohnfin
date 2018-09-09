package org.mifosplatform.mpesa.configuration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class MpesaInitializer extends SpringBootServletInitializer {

	public MpesaInitializer() {
		super();
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(MpesaConfiguration.class);
	}
}
