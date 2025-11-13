package ar.edu.utn.dds.k3003.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {

    private static final String CONNECTION_STRING = "mongodb+srv://fcristofaro_db_user:acsoszx4rRWbWn0M@dds-metamap-g3.m0eojp9.mongodb.net/?appName=DDS-MetaMap-G3";

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);


    @Bean
    public MongoClient mongoClient() {
        try {
            // Forzamos protocolo TLS 1.2 (por compatibilidad)
            System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .serverApi(serverApi)
                    .applyToSocketSettings(builder -> builder
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS))
                    .build();

            MongoClient client = MongoClients.create(settings);

            // Prueba de conexión simple (no obligatoria, pero útil para logs iniciales)
            client.listDatabaseNames().first();

            logger.info("✅ Conexión a MongoDB Atlas establecida correctamente.");
            return client;

        } catch (com.mongodb.MongoSocketWriteException e) {
            logger.error("❌ Error de escritura en el socket SSL al conectar con MongoDB Atlas: {}", e.getMessage());
        } catch (com.mongodb.MongoTimeoutException e) {
            logger.error("⚠️ Timeout al intentar conectar con MongoDB Atlas. Verificá red o firewall.");
        } catch (Exception e) {
            logger.error("❌ Error inesperado al inicializar conexión MongoDB: {}", e.getMessage());
        }

        // Devuelve un cliente “vacío” para no romper el contexto Spring
        return MongoClients.create();
    }

}