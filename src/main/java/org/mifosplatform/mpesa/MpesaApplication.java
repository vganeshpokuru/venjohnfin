package org.mifosplatform.mpesa;

import org.mifosplatform.mpesa.configuration.MpesaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class MpesaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpesaConfiguration.class, args);
    }

}