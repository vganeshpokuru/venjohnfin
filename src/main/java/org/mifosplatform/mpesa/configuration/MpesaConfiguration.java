package org.mifosplatform.mpesa.configuration;


import org.mifosplatform.mpesa.domain.Mpesa;
import org.mifosplatform.mpesa.domain.MpesaTransaction;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {
        "org.mifosplatform.mpesa.repository"
})
@EntityScan(basePackageClasses = {
        Mpesa.class,
        MpesaTransaction.class
        
})
@ComponentScan(basePackages = {
        "org.mifosplatform.mpesa.controller",
        "org.mifosplatform.mpesa.service"
})
public class MpesaConfiguration {
	
	public MpesaConfiguration() {
		super();
	}
	
	@Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster() {
        final SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return multicaster;
    }

}
