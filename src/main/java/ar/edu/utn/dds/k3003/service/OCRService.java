package ar.edu.utn.dds.k3003.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class OCRService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "K86883722888957";

    public String procesarImagen(String urlImagen) {
        try {
            String endpoint = "https://api.ocr.space/parse/imageurl";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint)
                    .queryParam("apikey", apiKey)
                    .queryParam("url", urlImagen);

            ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // La respuesta de OCR.Space suele tener la estructura "ParsedResults"[0]["ParsedText"]
                List parsedResults = (List) response.getBody().get("ParsedResults");
                if (parsedResults != null && !parsedResults.isEmpty()) {
                    Map firstResult = (Map) parsedResults.get(0);
                    return (String) firstResult.get("ParsedText");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al procesar OCR: " + e.getMessage());
        }

        return "";
    }

}
