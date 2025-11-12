package ar.edu.utn.dds.k3003.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    private static final String CONNECTION_STRING =
            "mongodb+srv://fcristofaro_db_user:acsoszx4rRWbWn0M@dds-metamap-g3.m0eojp9.mongodb.net/?appName=DDS-MetaMap-G3";

    @Bean
    public MongoClient mongoClient() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }
}