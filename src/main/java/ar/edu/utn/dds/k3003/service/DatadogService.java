package ar.edu.utn.dds.k3003.service;

import com.datadog.api.client.ApiClient;
import com.datadog.api.client.v1.api.MetricsApi;
import com.datadog.api.client.v1.model.MetricsPayload;
import com.datadog.api.client.v1.model.Series;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DatadogService {

    private MetricsApi metricsApi;

    @PostConstruct
    public void init() {

        System.out.println("DATADOG Iniciando cliente...");

        try{
            ApiClient client = new ApiClient();

            // Configurar claves directamente en el cliente
            //client.addDefaultHeader("DD-API-KEY", System.getenv("DD_API_KEY"));
            client.addDefaultHeader("DD-API-KEY", "73cf4da820b55ccce933d7a63f336efb");
            //client.addDefaultHeader("DD-API-KEY", System.getenv("DD_APP_KEY"));
            client.addDefaultHeader("DD-APPLICATION-KEY", "48d055c33027ba49d9f065626e47b4fb7951ba04");

            // Endpoint correcto
            client.setBasePath("https://app.datadoghq.com");

            this.metricsApi = new MetricsApi(client);

            System.out.println("DATADOG inicializado correctamente!");

            // --- Check de conexión ---
            enviarMetricaTest();

        } catch (Exception e) {
            System.err.println("[Datadog] Error inicializando cliente:");
            e.printStackTrace();
        }

    }

    private void enviarMetricaTest() {
        System.out.println("[Datadog] Enviando métrica de prueba para validar conexión...");

        try {
            long timestamp = System.currentTimeMillis() / 1000;

            Series serie = new Series()
                    .metric("conexion.datadog.test")
                    .type("gauge")
                    .points(Collections.singletonList(
                            java.util.Arrays.asList((double) timestamp, 1.0)
                    ));

            MetricsPayload payload = new MetricsPayload()
                    .series(Collections.singletonList(serie));

            metricsApi.submitMetrics(payload);

            System.out.println("[Datadog] ✔ Conexión validada: métrica de prueba enviada.");

        } catch (Exception e) {
            System.err.println("[Datadog] ✘ No se pudo enviar la métrica de prueba. Error de conexión:");
            e.printStackTrace();
        }
    }

    public void enviarMetrica(String nombre, double valor) {
        long timestamp = System.currentTimeMillis() / 1000;

        Series serie = new Series()
                .metric(nombre)
                .type("gauge")
                .points(Collections.singletonList(
                        java.util.Arrays.asList((double) timestamp, valor)
                ));
        System.out.println(serie);

        MetricsPayload payload = new MetricsPayload()
                .series(Collections.singletonList(serie));

        try {
            metricsApi.submitMetrics(payload);
            System.out.println("Métrica enviada: " + nombre + " = " + valor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
