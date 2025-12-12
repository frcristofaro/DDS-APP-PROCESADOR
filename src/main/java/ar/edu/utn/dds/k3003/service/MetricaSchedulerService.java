package ar.edu.utn.dds.k3003.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricaSchedulerService {

    private final DatadogService datadog;

    public MetricaSchedulerService(DatadogService datadog) {
        this.datadog = datadog;
    }

    // Corre cada 60 segundos
    @Scheduled(fixedRate = 60000)
    public void enviarMetricaUsuarios() {

        // Ejemplo: enviamos un valor random para probar
        double valor = Math.random() * 100;

        datadog.enviarMetrica("miapp.usuarios_logueados", valor);
    }
}

