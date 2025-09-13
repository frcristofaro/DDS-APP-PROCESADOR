package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.app.ConexionHTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ConexionHTTP conexionHTTP() {
        return new ConexionHTTP();
    }
}