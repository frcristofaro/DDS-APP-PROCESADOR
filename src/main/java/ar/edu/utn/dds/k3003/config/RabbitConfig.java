package ar.edu.utn.dds.k3003.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String COLA_PDI = "cola_pdis";

    @Bean
    public Queue pdiQueue() {
        return new Queue(COLA_PDI, true);
    }
}

